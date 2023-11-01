package step.learning.ws;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;
import step.learning.dto.entities.AuthToken;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat", configurator = WebsocketConfigurator.class)
public class WebsocketController
{
    private static final Set<Session> _sessions = Collections.synchronizedSet(new HashSet<>());
    private final AuthTokenDao _auth_token_dao;

    @Inject
    public WebsocketController(AuthTokenDao auth_token_dao)
    {
        _auth_token_dao = auth_token_dao;
    }

    public static void Broadcast(String message)
    {
        _sessions.forEach(session -> { SendToSession(session, 201, message); });
    }

    public static void SendToSession(Session session, int status, String message)
    {
        JsonObject response = new JsonObject();

        response.addProperty("status", status);
        response.addProperty("data", message);

        try { session.getBasicRemote().sendText(response.toString()); }
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
        String culture = (String)sec.getUserProperties().get("culture");

        if(culture == null)
        {
            try { session.close(); }
            catch (IOException ignored) { }
        }
        else
        {
            session.getUserProperties().put("culture", culture);
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
                AuthToken token = _auth_token_dao.GetTokenByBearer(data);

                if(token == null)
                {
                    SendToSession(session, 403, "Token rejected");
                    return;
                }

                session.getUserProperties().put("nik", token.GetNik());

                SendToSession(session, 202, token.GetNik());
                break;
            case "chat":
                long timestamp = System.currentTimeMillis();
                String message_time = message + " (" + FormatTimestamp(timestamp) + ")";

                Broadcast(session.getUserProperties().get("nik") + ": " + data);
                break;
            default:
                SendToSession(session, 405, "Command unrecognized");
        }
    }

    @OnError
    public void onError(Throwable ex, Session session) { System.err.println("broadcast: " + ex.getMessage()); }


}