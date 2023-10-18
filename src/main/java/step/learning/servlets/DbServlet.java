package step.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dao.CallMeDao;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.IDbProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@Singleton
public class DbServlet extends HttpServlet
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;
    private final CallMeDao _call_me_dao;

    @Inject
    public DbServlet(IDbProvider db_provider, @Named("db-prefix") String db_prefix, CallMeDao call_me_dao)
    {
        _db_provider = db_provider;
        _db_prefix = db_prefix;
        _call_me_dao = call_me_dao;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        switch (req.getMethod().toUpperCase())
        {
            case "PATCH":
                doPath(req, resp);
                break;
            case "LINK":
                doLink(req, resp);
                break;
            case "UNLINK":
                doUnlink(req, resp);
                break;
            default:
                super.service(req, resp);
        }
    }

    protected void doLink(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.getWriter().print("LINK works!");
    }

    protected void doUnlink(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.getWriter().print("UNLINK works!");
    }

    protected void doPath(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.setContentType("application/json");

        String content_type = req.getContentType();

        if(content_type == null || !content_type.startsWith("application/json"))
        {
            resp.setStatus(415);
            resp.getWriter().print("\"Unsupported Medioa Type: only application/json'\"");
            return;
        }

        JsonObject reqest_body;

        try(Reader reader = new InputStreamReader(req.getInputStream())) { reqest_body = JsonParser.parseReader(reader).getAsJsonObject(); }
        catch(Exception ex)
        {
            resp.setStatus(400);
            resp.getWriter().print("\"Invalid JSON: object required'\"");
            return;
        }

        CallMe item;

        try { item = new CallMe(reqest_body); }
        catch (IllegalArgumentException ex)
        {
            resp.setStatus(422);
            resp.getWriter().printf("\"Unprocessed content: %s\"", ex.getMessage());
            return;
        }

        try { _call_me_dao.Add(item); }
        catch (IllegalAccessException ex)
        {
            resp.setStatus(500);
            resp.getWriter().print("\"Internal Server Error: details in server logs\"");
            return;
        }

        resp.setStatus(201);
        resp.getWriter().print(new Gson().toJson(item));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setAttribute("page-body", "db.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        JsonObject result = _call_me_dao.Install();

        resp.setContentType("application/json");
        resp.getWriter().print(result.toString());
    }
}