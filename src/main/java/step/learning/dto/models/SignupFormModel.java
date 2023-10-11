package step.learning.dto.models;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignupFormModel
{
    private static final SimpleDateFormat _form_date_format = new SimpleDateFormat("yyyy-MM-dd");
    private String _login;
    private String _name;
    private String _password;
    private String _password_repeat;
    private String _email;
    private Date _birthdate;
    private Boolean _is_agree;

    public SignupFormModel(HttpServletRequest request) throws ParseException
    {
        this.SetLogin(request.getParameter("reg-login"));
        this.SetName(request.getParameter("reg-name"));
        this.SetPassword(request.getParameter("reg-password"));
        this.SetPasswordRepeat(request.getParameter("reg-repeat"));
        this.SetEmail(request.getParameter("reg-email"));
        this.SetBirthdate(request.getParameter("reg-birthdate"));
        this.SetAgree(request.getParameter("reg-agree"));
    }

    public void SetLogin(String login) { this._login = login; }
    public void SetName(String name) { this._name = name; }
    public void SetPassword(String password) { this._password = password; }
    public void SetPasswordRepeat(String password_repeat) { this._password_repeat = password_repeat; }
    public void SetEmail(String email) { this._email = email; }
    public void SetBirthdate(Date birthdate) { this._birthdate = birthdate; }
    public void SetIsAgree(Boolean is_agree) { this._is_agree = is_agree; }

    public String GetLogin() { return _login; }
    public String GetName() { return _name; }
    public String GetPassword() { return _password; }
    public String GetPasswordRepeat() { return _password_repeat; }
    public String GetEmail() { return _email; }
    public Date GetBirthdate() { return _birthdate; }
    public Boolean GetIsAgree() { return _is_agree; }

    public Map<String, String> GetValidationErrorMessage()
    {
        Map<String, String> result = new HashMap<>();

        if(_login == null || _login.isEmpty())
            result.put("login", "Login cannot be empty!");
        return result;
    }

    public void SetBirthdate(String birthdate) throws ParseException
    {
        if (birthdate == null || birthdate.isEmpty())
            _birthdate = null;
        else
            SetBirthdate(_form_date_format.parse(birthdate));
    }

    public void SetAgree(String input)
    {
        this.SetIsAgree("on".equalsIgnoreCase(input) || "true".equalsIgnoreCase(input));
    }
}
