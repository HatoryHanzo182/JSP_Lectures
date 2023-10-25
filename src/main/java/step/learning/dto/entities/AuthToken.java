package step.learning.dto.entities;

import java.util.Date;

public class AuthToken
{
    private String _jti;
    private String _sub;
    private Date _iat;
    private Date _exp;

    public String get_jti() {
        return _jti;
    }

    public void set_jti(String _jti) {
        this._jti = _jti;
    }

    public String get_sub() {
        return _sub;
    }

    public void set_sub(String _sub) {
        this._sub = _sub;
    }

    public Date get_iat() {
        return _iat;
    }

    public void set_iat(Date _iat) {
        this._iat = _iat;
    }

    public Date get_exp() {
        return _exp;
    }

    public void set_exp(Date _exp) {
        this._exp = _exp;
    }
}
