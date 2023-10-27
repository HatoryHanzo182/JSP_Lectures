package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dto.entities.AuthToken;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class TemplatesServlet extends HttpServlet
{
    private static final byte[] _buffer = new byte[8192];
    private final Logger _logger;
    private final AuthTokenDao _auth_token_dao;

    @Inject
    public TemplatesServlet(Logger logger, AuthTokenDao auth_token_dao)
    {
        _logger = logger;
        _auth_token_dao = auth_token_dao;
    }

    private void SendResponse(HttpServletResponse resp, int status_code, String message) throws IOException
    {
        resp.setStatus(status_code);
        resp.setContentType("text/plain");
        resp.getWriter().print(message);
    }

    private String CheckAuthToken(HttpServletRequest req)
    {
        String token = req.getHeader("Authorization");

        if(token == null)
            return "Authorization header required";

        if(!token.startsWith("Bearer "))
            return "Authorization scheme only";

        token = token.replace("Bearer ", "");

        AuthToken auth_token  = _auth_token_dao.GetTokenByBearer(token);

        if(auth_token == null)
            return "Token rejected";

        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String token_check_error = this.CheckAuthToken(req);

        if(token_check_error != null)
        {
            SendResponse(resp, 401, token_check_error);
            return;
        }

        String tpl_name = req.getPathInfo();

        if(tpl_name == null || tpl_name.isEmpty())
        {
            SendResponse(resp, 400, "Resource name required");
            return;
        }

        URL tpl_url = this.getClass().getClassLoader().getResource("tpl" + tpl_name);
        Path tpl_path;

        try
        {
            if(tpl_url == null || !Files.isRegularFile( tpl_path = Paths.get(tpl_url.toURI())))
            {
                SendResponse(resp, 404, "Resource not located");
                return;
            }
        }
        catch(URISyntaxException ignored)
        {
            SendResponse( resp, 400, "Resource name invalid" ) ;
            return ;
        }

        try(InputStream tpl_stream = tpl_url.openStream())
        {
            int bytes_read;
            OutputStream resp_stream = resp.getOutputStream();

            resp.setContentType(Files.probeContentType(tpl_path));

            while((bytes_read = tpl_stream.read(_buffer)) > 0)
            {
                resp_stream.write(_buffer, 0, bytes_read);
            }
            resp_stream.close();
        }
        catch(IOException ex)
        {
            _logger.log(Level.SEVERE, ex.getMessage() + " -- " + tpl_name);
            SendResponse(resp, 500, "Look at server logs");
        }
    }
}