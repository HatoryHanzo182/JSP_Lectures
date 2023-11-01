package step.learning.ws;

import com.google.inject.Inject;
import com.google.inject.Injector;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.lang.reflect.Field;

public class WebsocketConfigurator extends ServerEndpointConfig.Configurator
{
    @Inject
    private static Injector _injector;

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException
    {
        return _injector.getInstance(endpointClass);
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response)
    {
        super.modifyHandshake(sec, request, response);

        HttpServletRequest http_request = null;

        for(Field field : request.getClass().getDeclaredFields())
        {
            if(HttpServletRequest.class.isAssignableFrom(field.getType()))
            {
                field.setAccessible(true);

                try
                {
                    http_request = (HttpServletRequest) field.get(request);
                    break;
                }
                catch (IllegalAccessException ignored) { }
            }
        }

        if(http_request != null)
        {
            String culture = (String)http_request.getAttribute("culture");

            sec.getUserProperties().put("culture", culture);
        }
    }
}
