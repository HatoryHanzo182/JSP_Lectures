package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.AuthTokenDao;
import step.learning.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class AuthServlet extends HttpServlet
{
    private final UserDao _user_dao;
    private final AuthTokenDao _auth_token_dao;

    @Inject
    public AuthServlet(UserDao user_dao, AuthTokenDao auth_token_dao)
    {
        _user_dao = user_dao;
        _auth_token_dao = auth_token_dao;
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
