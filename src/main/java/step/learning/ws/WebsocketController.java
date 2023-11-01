package step.learning.ws;

import com.google.inject.Inject;
import step.learning.dao.AuthTokenDao;

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
        _sessions.forEach(session ->
        {
            try { session.getBasicRemote().sendText(message); }
            catch (Exception ex) { System.err.println("broadcast: " + ex.getMessage()); }
        });
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
        long timestamp = System.currentTimeMillis();
        String message_time = message + " (" + FormatTimestamp(timestamp) + ")";

        Broadcast(message_time + session.getUserProperties().get("culture"));
    }

    @OnError
    public void onError(Throwable ex, Session session) { System.err.println("broadcast: " + ex.getMessage()); }


}