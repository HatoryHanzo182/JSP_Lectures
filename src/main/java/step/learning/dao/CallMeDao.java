package step.learning.dao;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.IDbProvider;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class CallMeDao
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;
    private final Logger _logger;

    @Inject
    public CallMeDao(IDbProvider db_provider, @Named("db-prefix")String db_prefix, Logger logger)
    {
        _db_provider = db_provider;
        _db_prefix = db_prefix;
        _logger = logger;
    }

    public void Add(CallMe item) throws IllegalAccessException
    {
        String sql = "INSERT INTO " + _db_prefix + "call_me (`name`, `phone`) VALUES(?,?)";

        try (PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, item.GetName() );
            prep.setString(2, item.GetPhone() );
            prep.execute();
        }
        catch( SQLException ex )
        {
            _logger.log( Level.WARNING, ex.getMessage() + " -- " + sql ) ;
            throw new IllegalArgumentException( ex ) ;
        }
    }

    public JsonObject Install()
    {
        String sql = "CREATE TABLE " + _db_prefix + "call_me (" +
                "id BIGINT PRIMARY KEY," +
                "name VARCHAR(64) NULL," +
                "phone CHAR(13) NOT NULL COMMENT '+380000000000'," +
                "moment DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "call_moment DATETIME NULL" +
                ") ENGINE = InnoDB DEFAULT CHARSET = UTF8";
        JsonObject result = new JsonObject();

        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            statement.execute(sql);
            result.addProperty("status", "ok");
            result.addProperty("message", "create ok");
        }
        catch (SQLException ex)
        {
            _logger.log(Level.WARNING, ex.getMessage());

            if (ex.getSQLState().equals("42S01") && ex.getErrorCode() == 1050)
            {
                result.addProperty("status", "error");
                result.addProperty("message", "Ошибка: Таблица уже существует");
            }
            else
            {
                result.addProperty("status", "error");
                result.addProperty("message", "Произошла ошибка: " + ex.getMessage());
            }
        }
        catch (Exception ex)
        {
            result.addProperty("status", "error");
            result.addProperty("message", "Произошла ошибка: " + ex.getMessage());
        }

        return result;
    }

    public List<CallMe> GetAll() { return GetAll(false); }

    public List<CallMe> GetAll(boolean include_deleted)
    {
        List<CallMe> res = new ArrayList<>();
        String sql = "SELECT * FROM " + _db_prefix + "call_me";

        if (!include_deleted)
            sql += " WHERE delete_moment IS NULL";

        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            ResultSet result_set = statement.executeQuery(sql);

            while(result_set.next())
            {
                res.add(new CallMe(result_set));
            }
        }
        catch (SQLException ex) { _logger.log(Level.WARNING, ex.getMessage()); }

        return res;
    }

    public CallMe GetById(String id) { return GetById(id, false); }

    public CallMe GetById(String id, boolean include_deleted)
    {
        String sql = "SELECT * FROM " + _db_prefix + "call_me WHERE id = ?";

        if (!include_deleted)
            sql += " AND delete_moment IS NULL";

        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, id);

            ResultSet resultSet = prep.executeQuery();

            if(resultSet.next())
                return new CallMe(resultSet);
        }
        catch (Exception ex) { _logger.log(Level.WARNING, ex.getMessage() + " -- " + sql ); }

        return null;
    }

    public boolean SetCallMoment(CallMe item)
    {
        String sql = "SELECT CURRENT_TIMESTAMP";
        Timestamp timestamp;

        try( Statement statement = _db_provider.GetConnection().createStatement() )
        {
            ResultSet resultSet = statement.executeQuery(sql);

            resultSet.next();
            timestamp = resultSet.getTimestamp(1);
            item.SetCallMoment(new Date(timestamp.getTime()));
        }
        catch( Exception ex )
        {
            _logger.log( Level.WARNING, ex.getMessage() + " -- " + sql );
            return false ;
        }

        sql = "UPDATE " + _db_prefix + "call_me SET call_moment = ? WHERE id = ?";

        try( PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setTimestamp(1, timestamp);
            prep.setString(2, item.GetId());
            prep.execute();
            return true;
        }
        catch( Exception ex ) { _logger.log( Level.WARNING, ex.getMessage() + " -- " + sql ); }

        return false ;
    }

    public boolean Delete(CallMe item) { return Delete(item, false); }

    public boolean Delete(CallMe item, boolean hard_delete)
    {
        String sql;

        if(hard_delete)
            sql = "DELETE FROM " + _db_prefix + "call_me WHERE id = ?";
        else
            sql = "UPDATE " + _db_prefix + "call_me SET delete_moment = CURRENT_TIMESTAMP WHERE id = ?";

        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, item.GetId());
            prep.execute();
            return true;
        }
        catch (SQLException ex)
        {
            _logger.log(Level.WARNING, ex.getMessage() + " -- " + sql );
            return false;
        }
    }
}