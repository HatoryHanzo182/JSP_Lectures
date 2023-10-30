package step.learning.ws;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/chat")
public class WebsocketServer
{
    private static final Set<Session> _sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session)
    {
        _sessions.add(session);
    }

    @OnClose
    public void onClose(Session session)
    {
        _sessions.remove(session);
    }

    @OnMessage
    public void onMessage(String message, Session session)
    {

    }

    @OnError
    public void onError(Throwable ex, Session session)
    {

    }
}