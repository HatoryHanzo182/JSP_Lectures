package step.learning.ws;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.ChatDao;
import step.learning.dto.entities.AuthToken;
import step.learning.dto.entities.ChatMessage;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@ServerEndpoint(value = "/chat", configurator = WebsocketConfigurator.class)
public class WebsocketController
{
    private static final Set<Session> _sessions = Collections.synchronizedSet(new HashSet<>());
    private final AuthTokenDao _auth_token_dao;
    private final ChatDao _chat_dao;

    @Inject
    public WebsocketController(AuthTokenDao auth_token_dao, ChatDao chat_dao)
    {
        _auth_token_dao = auth_token_dao;
        _chat_dao = chat_dao;
    }

    public static void Broadcast(String message)
    {
        JsonObject response = new JsonObject();
        response.addProperty("status", 201);
        response.addProperty("data", message);

        _sessions.forEach(session ->
        {
            try { session.getBasicRemote().sendText(response.toString()); }
            catch (Exception ex) { System.err.println("SendToSession: " + ex.getMessage()); }
        });
        // _sessions.forEach(session -> { SendToSession(session, 201, message); });
    }

    public static void SendToSession(Session session, int status, String message)
    {
        JsonObject response = new JsonObject();

        response.addProperty("status", status);
        response.addProperty("data", message);

        try { session.getBasicRemote().sendText(response.toString()); }
        catch (Exception ex) { System.err.println("SendToSession: " + ex.getMessage()); }
    }

    public static void SendToSession(Session session, JsonObject jsonObject)
    {
        try { session.getBasicRemote().sendText(jsonObject.toString()); }
        catch (Exception ex) { System.err.println("SendToSession: " + ex.getMessage()); }
    }

    private String FormatTimestamp(long timestamp)
    {
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        return sdf.format(date);
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig sec)
    {
        _chat_dao.Install();

        String culture = (String) sec.getUserProperties().get("culture");

        if (culture == null)
        {
            try { session.close(); }
            catch (IOException ignored) { }
        }
        else
        {
            session.getUserProperties().put("culture", culture);

            List<ChatMessage> lastMessages = _chat_dao.GetLastMessages(5);

            for (ChatMessage message : lastMessages)
            {
                String formatted_message = message.GetMessage() + " (" + FormatTimestamp(message.GetMoment().getTime()) + ")";

                SendToSession(session, 201, formatted_message);
            }

            _sessions.add(session);
        }
    }

    @OnClose
    public void onClose(Session session) { _sessions.remove(session); }

    @OnMessage
    public void onMessage(String message, Session session)
    {
        JsonObject request = JsonParser.parseString(message).getAsJsonObject();
        String command = request.get("command").getAsString();
        String data = request.get("data").getAsString();

        switch(command)
        {
            case "auth":
            {
                AuthToken token = _auth_token_dao.GetTokenByBearer(data);

                if (token == null) {
                    SendToSession(session, 403, "Token rejected");
                    return;
                }

                session.getUserProperties().put("token", token);

                SendToSession(session, 202, token.GetNik());
                break;
            }
            case "chat":
            {
                AuthToken token = (AuthToken) session.getUserProperties().get("token");
                ChatMessage chat_message = new ChatMessage(token.GetSub(), data);
                long timestamp = System.currentTimeMillis();
                String message_time = token.GetNik() + ": " + data + "  (" + FormatTimestamp(timestamp) + ")";

                _chat_dao.Add(chat_message);

                Broadcast(message_time);

                _auth_token_dao.RenewToken(token);
                break;
            }
            case "load":
            {
                AuthToken token = (AuthToken) session.getUserProperties().get("token");

                if(token != null)
                {
                    JsonObject response = new JsonObject();
                    JsonArray array = new JsonArray();

                    for(ChatMessage msg : _chat_dao.GetLastMessages(5))
                        array.add(msg.ToJsonObject());

                    response.addProperty("status", 200);
                    response.add("data", array);

                    SendToSession(session, response);
                }
                else
                {
                    SendToSession(session, 403, "Token rejected.");
                }
            }
            default:
                SendToSession(session, 405, "Command unrecognized");
        }
    }

    @OnError
    public void onError(Throwable ex, Session session) { System.err.println("broadcast: " + ex.getMessage()); }
}