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
        this.set_id(result_set.getString("id"));
        this.set_login(result_set.getString("login"));
        this.set_real_name(result_set.getString("real_name"));
        this.set_email(result_set.getString("email"));
        this.set_salt(result_set.getString("salt"));
        this.set_pass_dk(result_set.getString("pass_dk"));
        this.set_birthday(result_set.getDate("birthday"));
        this.set_avatar(result_set.getString("avatar_url"));
        this.set_register_moment(result_set.getDate("register_moment"));

        Timestamp timestamp = result_set.getTimestamp("register_moment") ;
        if( timestamp != null ) {
            this.set_register_moment( new Date( timestamp.getTime() ) ); ;
        }
        timestamp = result_set.getTimestamp("delete_moment") ;
        if( timestamp != null ) {
            this.set_delete_moment( new Date( timestamp.getTime() ) ); ;
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_login() {
        return _login;
    }

    public void set_login(String _login) {
        this._login = _login;
    }

    public String get_real_name() {
        return _real_name;
    }

    public void set_real_name(String _real_name) {
        this._real_name = _real_name;
    }

    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public String get_salt() {
        return _salt;
    }

    public void set_salt(String _salt) {
        this._salt = _salt;
    }

    public String get_pass_dk() {
        return _pass_dk;
    }

    public void set_pass_dk(String _pass_dk) {
        this._pass_dk = _pass_dk;
    }

    public Date get_birthday() {
        return _birthday;
    }

    public void set_birthday(Date _birthday) {
        this._birthday = _birthday;
    }

    public String get_avatar() {
        return _avatar;
    }

    public void set_avatar(String _avatar) {
        this._avatar = _avatar;
    }

    public Date get_register_moment() {
        return _register_moment;
    }

    public void set_register_moment(Date _register_moment) {
        this._register_moment = _register_moment;
    }

    public Date get_delete_moment() {
        return _delete_moment;
    }

    public void set_delete_moment(Date _delete_moment) {
        this._delete_moment = _delete_moment;
    }
}
