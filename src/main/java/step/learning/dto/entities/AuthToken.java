package step.learning.dto.entities;

import java.util.Date;

public class AuthToken
{
    private String _jti;
    private String _sub;
    private Date _iat;
    private Date _exp;

    public void SetJti(String _jti) { this._jti = _jti; }
    public void SetSub(String _sub) { this._sub = _sub; }
    public void SetIat(Date _iat) { this._iat = _iat; }
    public void SetExp(Date _exp) { this._exp = _exp; }

    public String GetJti() { return _jti; }
    public String GetSub() { return _sub; }
    public Date GetIat() { return _iat; }
    public Date GetExp() { return _exp; }
}