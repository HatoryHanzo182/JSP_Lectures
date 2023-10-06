package step.learning.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HomeServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        // resp.getWriter().print("HomeServlet");
        req.setAttribute("page-body", "index.jsp");
        req.getRequestDispatcher("WEB-INF/_layout.jsp").forward(req, resp);
    }
}
