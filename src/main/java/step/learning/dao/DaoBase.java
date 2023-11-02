package step.learning.dao;

import com.google.inject.Inject;
import step.learning.services.db.IDbProvider;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoBase
{
    private final Logger _logger;
    private final IDbProvider _db_provider;

    @Inject
    public DaoBase(Logger logger, IDbProvider db_provider)
    {
        _logger = logger;
        _db_provider = db_provider;
    }

    protected Timestamp GetDbTimestamp()
    {
        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            ResultSet result_set = statement.executeQuery("SELECT CURRENT_TIMESTAMP");

            result_set.next();
            return result_set.getTimestamp(1);
        }
        catch (Exception ex) { _logger.log(Level.WARNING, ex.getMessage()); }

        return null;
    }

    protected String GetDbIdentity()
    {
        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            ResultSet result_set = statement.executeQuery("SELECT UUID_SHORT()");

            result_set.next();
            return result_set.getString(1);
        }
        catch (Exception ex) { _logger.log(Level.WARNING, ex.getMessage()); }

        return null;
    }
}