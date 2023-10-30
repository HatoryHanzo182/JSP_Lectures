package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.filters.CultureFilter;
import step.learning.servlets.*;

public class RouterModule extends ServletModule
{
    @Override
    protected void configureServlets()
    {
        filter("/*").through(CharsetFilter.class);
        filter("/*").through(CultureFilter.class);
        serve("/").with(HomeServlet.class);
        serve("/jsp").with(AboutServlet.class);
        serve("/filters").with(FiltersServlet.class);
        serve("/ioc").with(IocServlet.class);
        serve("/signup").with(SignupServlet.class);
        serve("/auth").with(AuthServlet.class);
        serveRegex("/\\w\\w/").with(HomeServlet.class);
        serveRegex("/\\w\\w/filters").with(FiltersServlet.class);
        serveRegex("/\\w\\w/ioc").with(IocServlet.class);
        serveRegex("/\\w\\w/signup").with(SignupServlet.class);
        serveRegex("/\\w\\w/db").with(DbServlet.class);
        serveRegex("/\\w\\w/spa").with(SpaServlet.class);
        serveRegex("/\\w\\w/tpl(/.*)").with(TemplatesServlet.class);
        serveRegex("/\\w\\w/ws").with(WsServlet.class);
    }
}