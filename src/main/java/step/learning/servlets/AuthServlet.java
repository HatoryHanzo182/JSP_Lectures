package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.UserDao;
import step.learning.dto.entities.AuthToken;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

@Singleton
public class AuthServlet extends HttpServlet
{
    private final static Gson _gson = new GsonBuilder().serializeNulls().create();
    private final UserDao _user_dao;
    private final AuthTokenDao _auth_token_dao;

    @Inject
    public AuthServlet(UserDao user_dao, AuthTokenDao auth_token_dao)
    {
        _user_dao = user_dao;
        _auth_token_dao = auth_token_dao;
    }

    private void SendResponse(HttpServletResponse resp, int status_code, Object body) throws IOException
    {
        resp.setStatus(status_code);
        resp.setContentType("application/json");
        _gson.toJson(body, resp.getWriter());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        if(login == null || password == null)
        {
            SendResponse(resp, 400, "login and password are required");
            return;
        }
        if(login.isEmpty() || password.isEmpty())
        {
            SendResponse(resp, 400, "login and password could not br empty");
            return;
        }

        AuthToken token = _auth_token_dao.GetTokenByCredentials(login, password);

        if(token == null)
        {
            SendResponse(resp, 401, "Credentials rejected");
            return;
        }

        String json_token = _gson.toJson(token);
        String encoding_token = Base64.getUrlEncoder().encodeToString(json_token.getBytes());

        SendResponse(resp, 202, encoding_token);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        if(_auth_token_dao.Install())
            resp.getWriter().print("Created");
        else
            resp.getWriter().print("Error");
    }
}