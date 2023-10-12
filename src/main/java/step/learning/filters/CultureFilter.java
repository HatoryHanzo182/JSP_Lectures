package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Singleton
public class CultureFilter  implements Filter
{
    private FilterConfig _filter_config;

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

        if( matcher.matches() )
            req.setAttribute( "culture", matcher.group( 1 ) ) ;
        else
            req.setAttribute( "culture", "uk" ) ;

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy()
    {
        _filter_config = null;
    }
}
