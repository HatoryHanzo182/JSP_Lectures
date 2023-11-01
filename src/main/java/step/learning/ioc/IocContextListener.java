package step.learning.ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import step.learning.ws.WebsocketModule;

public class IocContextListener extends GuiceServletContextListener
{
    @Override
    protected Injector getInjector()
    {
        return Guice.createInjector(new RouterModule(), new ServicesModule(), new LoggingModule(), new WebsocketModule());
    }
}
