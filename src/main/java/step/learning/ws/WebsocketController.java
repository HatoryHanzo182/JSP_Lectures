package step.learning.ws;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat")
public class WebsocketController
{
    private static final Set<Session> _sessions = Collections.synchronizedSet(new HashSet<>());

    public static void Broadcast(String message)
    {
        _sessions.forEach(session ->
        {
            try { session.getBasicRemote().sendText(message); }
            catch (Exception ex) { System.err.println("broadcast: " + ex.getMessage()); }
        });
    }

    @OnOpen
    public void onOpen(Session session) { _sessions.add(session); }

    @OnClose
    public void onClose(Session session) { _sessions.remove(session); }

    @OnMessage
    public void onMessage(String message, Session session) { Broadcast(message); }

    @OnError
    public void onError(Throwable ex, Session session) { System.err.println("broadcast: " + ex.getMessage()); }
}