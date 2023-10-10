package step.learning.ioc;

import com.google.inject.servlet.ServletModule;
import step.learning.filters.CharsetFilter;
import step.learning.servlets.AboutServlet;
import step.learning.servlets.FiltersServlet;
import step.learning.servlets.HomeServlet;
import step.learning.servlets.IocServlet;

public class RouterModule extends ServletModule
{
    @Override
    protected void configureServlets()
    {
        filter("/*").through(CharsetFilter.class);
        serve("/").with(HomeServlet.class);
        serve("/jsp").with(AboutServlet.class);
        serve("/filters").with(FiltersServlet.class);
        serve("/ioc").with(IocServlet.class);
    }
}
