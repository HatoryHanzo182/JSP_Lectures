package step.learning.filters;

import com.google.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Singleton
public class CharsetFilter implements Filter
{
    private FilterConfig _filter_config;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { _filter_config = filterConfig; }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException
    {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res= (HttpServletResponse) servletResponse;
        String charset = StandardCharsets.UTF_8.name();

        req.setCharacterEncoding(charset);
        res.setCharacterEncoding(charset);
        req.setAttribute("charset", charset);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() { _filter_config = null; }
}
