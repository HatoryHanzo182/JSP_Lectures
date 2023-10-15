package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.culture.IResourceProvider;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class CultureFilter  implements Filter
{
    private FilterConfig _filter_config;

    private final IResourceProvider resource_provider;

    @Inject
    public CultureFilter(IResourceProvider resourceProvider)
    {
        resource_provider = resourceProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
        _filter_config = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String uri = req.getServletPath() ;
        Matcher matcher = Pattern.compile("^/(\\w\\w)/(.*)$").matcher(uri);
        String culture = "uk";

        if( matcher.matches() )
            culture = matcher.group( 1 );

        req.setAttribute( "culture", culture );
        resource_provider.SetCulture(culture);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy()
    {
        _filter_config = null;
    }
}