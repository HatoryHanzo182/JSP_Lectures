package step.learning.dto.entities;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ChatMessage
{
    private String _id;
    private String _sender_id;
    private String _message;
    private Date _moment;

    public ChatMessage()
    {

    }

    public ChatMessage(ResultSet result_set) throws SQLException
    {
        SetId(result_set.getString("id"));
        SetSenderId(result_set.getString("sender_id"));
        SetMessage(result_set.getString("message"));
        SetMoment(new Date(result_set.getTimestamp("moment").getTime()));
    }

    public void SetId(String id) { this._id = id; }
    public void SetSenderId(String sender_id) { this._sender_id = sender_id; }
    public void SetMessage(String message) { this._message = message; }
    public void SetMoment(Date moment) { this._moment = moment; }

    public String GetId() { return _id; }
    public String GetSenderId() { return _sender_id; }
    public String GetMessage() { return _message; }
    public Date GetMoment() { return _moment; }
}