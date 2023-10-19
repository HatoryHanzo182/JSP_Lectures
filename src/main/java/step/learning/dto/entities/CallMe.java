package step.learning.dto.entities;

import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.regex.Pattern;

public class CallMe
{
    private String _id;
    private String _name;
    private String _phone;
    private Date _moment;
    private Date _call_moment;

    public CallMe()
    {

    }

    public CallMe(JsonObject json_object) throws IllegalArgumentException
    {
        try
        {
            SetName(json_object.get("name").getAsString());
            SetPhone(json_object.get("phone").getAsString());
        }
        catch (IllegalArgumentException ex) { throw ex; }
        catch(Exception ex) { throw new IllegalArgumentException("JSON object must hav non-null 'name' and 'phone'"); }
    }

    public CallMe(ResultSet result_set) throws IllegalArgumentException
    {
        try
        {
            SetId(result_set.getString("id"));
            SetName(result_set.getString("name"));
            SetPhone(result_set.getString("phone"));
            SetMoment(new Date(result_set.getTimestamp("moment").getTime()));

            Timestamp call_moment_timestamp = result_set.getTimestamp("call_moment");

            if(call_moment_timestamp != null)
                SetCallMoment(result_set.getDate("call_moment"));
        }
        catch (Exception ex) { throw new IllegalArgumentException(ex); }
    }

    public void SetId(String id) { this._id = id; }
    public void SetName(String name) { this._name = name; }
    public void SetPhone(String phone)
    {
        if (Pattern.matches("^\\+380([\\s-]?\\d){9}$", phone))
            _phone = phone.replaceAll("[\\s-]+", "");
        else
            throw new IllegalArgumentException("Phone must match '+380XXXXXXXXXX'patern\"");
    }
    public void SetMoment(Date moment) { this._moment = moment; }
    public void SetCallMoment(Date call_moment) { this._call_moment = call_moment; }

    public String GetId() { return _id; }
    public String GetName() { return _name; }
    public String GetPhone() { return _phone; }
    public Date GetMoment() { return _moment; }
    public Date GetCallMoment() { return _call_moment; }
}