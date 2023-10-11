package step.learning.servlets;

import com.google.inject.Singleton;
import step.learning.dto.models.SignupFormModel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

@Singleton
public class SignupServlet extends HttpServlet
{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        //String reg_data = (String)session.getAttribute("reg-data");
        Integer reg_status = (Integer)session.getAttribute("reg-status") ;

        if(reg_status != null)
        {
            session.removeAttribute("reg-status") ;
            req.setAttribute("reg-data", reg_status.toString());

            if(reg_status == 2)
                req.setAttribute("reg-model", session.getAttribute("reg-model"));
        }

        req.setAttribute("page-body", "signup.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        SignupFormModel form_model;

        try
        {
            form_model = new SignupFormModel(req);

            Map<String, String> validation_errors = form_model.GetValidationErrorMessage();

            if (validation_errors.isEmpty()) { }
            else
            {
                session.setAttribute("reg-status", 2);
                session.setAttribute("reg-model", form_model);
                session.setAttribute("validation-errors", validation_errors);
            }
        }
        catch (ParseException ex) { session.setAttribute("reg-status", 1); }

        resp.sendRedirect(req.getRequestURI());
    }
}
