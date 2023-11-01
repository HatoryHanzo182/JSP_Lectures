package step.learning.dto.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class AuthToken
{
    private String _jti;
    private String _sub;
    private Date _iat;
    private Date _exp;
    private String _nik;

    public AuthToken()
    {

    }

    public AuthToken(ResultSet result_set) throws SQLException
    {
        this.SetJti(result_set.getString("jti"));
        this.SetSub(result_set.getString("sub"));
        this.SetIat(new Date(result_set.getTimestamp("iat").getTime()));
        this.SetExp(new Date(result_set.getTimestamp("exp").getTime()));

        try { this._nik = result_set.getString("nik"); }
        catch(Exception ignored) { }
    }

    public void SetJti(String _jti) { this._jti = _jti; }
    public void SetSub(String _sub) { this._sub = _sub; }
    public void SetIat(Date _iat) { this._iat = _iat; }
    public void SetExp(Date _exp) { this._exp = _exp; }

    public String GetJti() { return _jti; }
    public String GetSub() { return _sub; }
    public Date GetIat() { return _iat; }
    public Date GetExp() { return _exp; }
    public String GetNik() { return _nik; }
}