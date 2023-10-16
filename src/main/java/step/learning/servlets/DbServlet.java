package step.learning.servlets;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.services.db.IDbProvider;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton
public class DbServlet extends HttpServlet
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;

    @Inject
    public DbServlet(IDbProvider db_provider, @Named("db-prefix") String db_prefix)
    {
        _db_provider = db_provider;
        _db_prefix = db_prefix;
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
        String sql = "CREATE TABLE " + _db_prefix + "call_me (" +
                "id BIGINT PRIMARY KEY," +
                "name VARCHAR(64) NULL," +
                "phone CHAR(13) NOT NULL COMMENT '+380000000000'" +
                ") ENGINE = InnoDB DEFAULT CHARSET = UTF8";
        JsonObject result = new JsonObject();

        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            statement.executeUpdate(sql);
            result.addProperty("status", "ok");
            result.addProperty("message", "create ok");
        }
        catch (SQLException ex)
        {
            result.addProperty("status", "error");
            result.addProperty("message", ex.getMessage());
        }
        resp.getWriter().print(result);
    }
}
