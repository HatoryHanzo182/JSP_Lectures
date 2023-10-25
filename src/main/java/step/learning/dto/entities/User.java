package step.learning.dto.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class User
{
    private String _id;
    private String _login;
    private String _real_name;
    private String _email;
    private String _salt;
    private String _pass_dk;  // RFC 2898 DK - derived key;
    private Date _birthday;
    private String _avatar;
    private Date _register_moment;
    private Date _delete_moment;

    public User()
    {

    }

    public User(ResultSet result_set) throws SQLException
    {
        this.SetId(result_set.getString("id"));
        this.SetLogin(result_set.getString("login"));
        this.SetRealName(result_set.getString("real_name"));
        this.SetEmail(result_set.getString("email"));
        this.SetSalt(result_set.getString("salt"));
        this.SetPassDk(result_set.getString("pass_dk"));
        this.SetBirthday(result_set.getDate("birthday"));
        this.SetAvatar(result_set.getString("avatar_url"));

        Timestamp timestamp = result_set.getTimestamp("register_moment") ;

        if( timestamp != null )
            this.SetRegisterMoment( new Date( timestamp.getTime() ) );

        timestamp = result_set.getTimestamp("delete_moment") ;

        if( timestamp != null )
            this.SetDeleteMoment( new Date( timestamp.getTime() ) );
    }

    public void SetId(String id) { this._id = id; }
    public void SetLogin(String login) { this._login = login; }
    public void SetRealName(String real_name) { this._real_name = real_name; }
    public void SetEmail(String email) { this._email = email; }
    public void SetSalt(String salt) { this._salt = salt; }
    public void SetPassDk(String pass_dk) { this._pass_dk = pass_dk; }
    public void SetBirthday(Date birthday) { this._birthday = birthday; }
    public void SetAvatar(String avatar) { this._avatar = avatar; }
    public void SetRegisterMoment(Date register_moment) { this._register_moment = register_moment; }
    public void SetDeleteMoment(Date delete_moment) { this._delete_moment = delete_moment; }

    public String GetId() { return _id; }
    public String GetLogin() { return _login; }
    public String GetRealName() { return _real_name; }
    public String GetEmail() { return _email; }
    public String GetSalt() { return _salt; }
    public String GetPassDk() { return _pass_dk; }
    public Date GetBirthday() { return _birthday; }
    public String GetAvatar() { return _avatar; }
    public Date GetRegisterMoment() { return _register_moment; }
    public Date GetDeleteMoment() { return _delete_moment; }
}