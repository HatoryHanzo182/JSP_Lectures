package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.services.db.IDbProvider;

import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AuthTokenDao
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;
    private final Logger _logger;

    @Inject
    public AuthTokenDao(IDbProvider db_provider, @Named("db-prefix") String db_prefix, Logger logger)
    {
        _db_provider = db_provider;
        _db_prefix = db_prefix;
        _logger = logger;
    }

    public boolean Install()
    {
        String sql = "CREATE TABLE " + _db_prefix + "auth_tokens (" +
                "`jti` BINARY(16) PRIMARY KEY DEFAULT (UUID_TO_BIN(UUID()))," +
                "`sub` BIGINT UNSIGNED NOT NULL," +
                "`iat` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "`exp` DATETIME NOT NULL" +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_unicode_ci";

        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            statement.executeUpdate(sql);
            return true;
        }
        catch (Exception ex) { _logger.log(Level.WARNING, ex.getMessage() + " - - " + sql); }
        return false;
    }
}
