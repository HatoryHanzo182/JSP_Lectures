package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import step.learning.dto.entities.ChatMessage;
import step.learning.services.db.IDbProvider;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatDao extends DaoBase
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;
    private final Logger _logger;

    @Inject
    public ChatDao(IDbProvider db_provider, @Named("db-prefix")String db_prefix, Logger logger)
    {
        super(logger, db_provider);

        this._db_provider = db_provider;
        this._db_prefix = db_prefix;
        this._logger = logger;
    }

    public void Install()
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + _db_prefix + "chat_messages (" +
                "id BIGINT UNSIGNED  PRIMARY KEY DEFAULT(UUID_SHORT()), " +
                "sender_id BIGINT UNSIGNED NOT NULL, " +
                "message TEXT NOT NULL, " +
                "moment DATETIME NOT NULL  DEFAULT CURRENT_TIMESTAMP" +
                ") ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_unicode_ci";

        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            statement.executeUpdate(sql);
        }
        catch(SQLException ex){ _logger.log(Level.WARNING, ex.getMessage() + " -- " + sql); }
    }

    public boolean Add(ChatMessage chat_message)
    {
        chat_message.SetMoment(new Date(super.GetDbTimestamp().getTime()));
        chat_message.SetId( super.GetDbIdentity());
        String sql = "INSERT INTO " + _db_prefix + "chat_messages (`id`, `sender_id`, `message`, `moment`)" +
                "VALUES(?,?,?,?)";

        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, chat_message.GetId());
            prep.setString(2, chat_message.GetSenderId());
            prep.setString(3, chat_message.GetMessage());
            prep.setTimestamp(4, new Timestamp(chat_message.GetMoment().getTime()));
            prep.executeUpdate();

            return true;
        }
        catch(SQLException ex)
        {
            _logger.log(Level.WARNING, ex.getMessage() + "---" + sql);
            return false;
        }
    }
}
