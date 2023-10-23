package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.UserDao;
import step.learning.dto.models.SignupFormModel;
import step.learning.services.culture.IResourceProvider;
import step.learning.services.formparse.IFormParsResult;
import step.learning.services.formparse.IFormParsService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SignupServlet extends HttpServlet
{
    private final IResourceProvider _resource_provider;
    private final IFormParsService _form_pars_service;
    private final UserDao _user_dao;

    @Inject
    public SignupServlet(IResourceProvider resource_provider, IFormParsService formParsService, UserDao user_dao)
    {
        _resource_provider = resource_provider;
        _form_pars_service = formParsService;
        _user_dao = user_dao;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        HttpSession session = req.getSession();
        Integer reg_status = (Integer)session.getAttribute("reg-status") ;

        if(reg_status != null)
        {
            session.removeAttribute("reg-status") ;
            req.setAttribute("reg-data", reg_status.toString());

            if(reg_status == 2)
            {
                SignupFormModel form_model = (SignupFormModel) session.getAttribute( "reg-model" ) ;

                req.setAttribute("reg-model", session.getAttribute("reg-model"));

                Map<String, String> validation_errors = form_model == null ? new HashMap<String, String>() : form_model.GetValidationErrorMessage();

                for (String key : validation_errors.keySet())
                {
                    String translation = _resource_provider.GetString(validation_errors.get(key));

                    if (translation != null)
                        validation_errors.put(key, translation);
                }
                req.setAttribute("validation_errors", validation_errors);

                if(form_model != null && validation_errors.isEmpty())
                    _user_dao.AddFromForm(form_model);
            }
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
            IFormParsResult form_pars_result = _form_pars_service.Parse(req);

            form_model = new SignupFormModel(form_pars_result);

            Map<String, String> validation_errors = form_model.GetValidationErrorMessage();

            if (validation_errors.isEmpty())
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
