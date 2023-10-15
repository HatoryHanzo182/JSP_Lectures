package step.learning.dto.models;

import org.apache.commons.fileupload.FileItem;
import step.learning.services.formparse.IFormParsResult;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

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
    private String _avatar;

    public SignupFormModel(IFormParsResult form_parse_result) throws ParseException
    {
        Map<String, String> fields = form_parse_result.GetFields();

        this.SetLogin(fields.get("reg-login"));
        this.SetName(fields.get("reg-name"));
        this.SetPassword(fields.get("reg-password"));
        this.SetPasswordRepeat(fields.get("reg-repeat"));
        this.SetEmail(fields.get("reg-email"));
        this.SetBirthdate(fields.get("reg-birthdate"));
        this.SetAgree(fields.get("reg-agree"));
        this.SetAvatar(form_parse_result);
    }

    public void SetLogin(String login) { this._login = login; }
    public void SetName(String name) { this._name = name; }
    public void SetPassword(String password) { this._password = password; }
    public void SetPasswordRepeat(String password_repeat) { this._password_repeat = password_repeat; }
    public void SetEmail(String email) { this._email = email; }
    public void SetBirthdate(Date birthdate) { this._birthdate = birthdate; }
    public void SetIsAgree(Boolean is_agree) { this._is_agree = is_agree; }
    public void SetAvatar(String _avatar) { this._avatar = _avatar; }

    public String GetLogin() { return _login; }
    public String GetName() { return _name; }
    public String GetPassword() { return _password; }
    public String GetPasswordRepeat() { return _password_repeat; }
    public String GetEmail() { return _email; }
    public Date GetBirthdate() { return _birthdate; }
    public Boolean GetIsAgree() { return _is_agree; }
    public String GetAvatar() { return _avatar; }

    public Map<String, String> GetValidationErrorMessage()
    {
        Map<String, String> result = new HashMap<>();

        if(_login == null || _login.isEmpty())
            result.put("login", "signup_login_empty");
        else if(_login.length() < 2)
            result.put("login", "signup_login_too_short");
        else if(!Pattern.matches("^[a-zA-Z0-9]+$", _login))
            result.put("login", "signup_login_pattern_mismatch");
        if (_name == null || _name.isEmpty())
            result.put("name", "Name cannot be empty!");
        if (_email == null || _email.isEmpty())
            result.put("email", "Email cannot be empty!");
        if (_password == null || _password.isEmpty())
            result.put("password", "Password cannot be empty!");
        if (_password_repeat == null || !_password_repeat.equals(_password))
            result.put("password_repeat", "Passwords do not match!");
        if (_birthdate == null)
            result.put("birthdate", "Birthdate is required!");
        if (_is_agree == null || !_is_agree)
            result.put("agree", "You must agree to the terms!");

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

    public void SetAvatar(IFormParsResult form_parse_result)
    {
        Map<String, FileItem> files = form_parse_result.GetFiles();

        if (!files.containsKey("reg-avatar"))
        {
            this._avatar = null;
            return;
        }

        FileItem file_item = files.get("reg-avatar");
        String uploaded_filename = file_item.getName();
        int dot_index = uploaded_filename.lastIndexOf('.');
        String ext = uploaded_filename.substring(dot_index);
        String[] extensions = {".jpg", ".jpeg", ".png", ".ico", ".gif"};

        if(!Arrays.asList(extensions).contains(ext))
            throw new RuntimeException("Invalid file extension");

        String upload_DIR = form_parse_result.GetRequest().getServletContext().getRealPath("./upload/avatar/");
        String saved_filename;
        File saved_file;

        do
        {
            saved_filename = UUID.randomUUID().toString().substring(0, 8) + ext;
            saved_file = new File(upload_DIR, saved_filename);
        } while(saved_file.exists());

        try { file_item.write(saved_file); }
        catch (Exception ex) { throw new RuntimeException(ex); }

        this.SetAvatar(saved_filename);
    }
}