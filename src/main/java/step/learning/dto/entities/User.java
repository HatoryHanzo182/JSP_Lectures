package step.learning.dto.entities;

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
}
