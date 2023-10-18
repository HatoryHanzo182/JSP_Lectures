package step.learning.dao;

import com.google.gson.JsonObject;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.IDbProvider;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton
public class CallMeDao
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;

    @Inject
    public CallMeDao(IDbProvider db_provider, @Named("db-prefix")String db_prefix)
    {
        _db_provider = db_provider;
        _db_prefix = db_prefix;
    }

    public void Add(CallMe item) throws IllegalAccessException
    {
        String sql = "INSERT INTO " + _db_prefix + "call_me (`name`, `phone`) VALUES(?,?)";

        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, item.GetName());
            prep.setString(2, item.GetPhone());
            prep.execute();
        }
        catch (SQLException ex)
        {
            System.err.println(ex.getMessage());
            throw new IllegalArgumentException(ex);
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
}
