package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.AuthToken;
import step.learning.dto.entities.User;
import step.learning.services.db.IDbProvider;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AuthTokenDao
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;
    private final Logger _logger;
    private final UserDao _user_dao;

    @Inject
    public AuthTokenDao(IDbProvider db_provider, @Named("db-prefix") String db_prefix, Logger logger, UserDao user_dao)
    {
        _db_provider = db_provider;
        _db_prefix = db_prefix;
        _logger = logger;
        _user_dao = user_dao;
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

    public AuthToken GetTokenByCredentials(String login, String password)
    {
        User user = _user_dao.GetUserByCredentials(login, password);

        if(user == null)
            return null;

        AuthToken token = new AuthToken();

        token.set_jti(UUID.randomUUID().toString());
        token.set_sub(user.get_id());

        Timestamp now = GetDbTimestamp();

        if(now == null)
            return null;

        token.set_iat(new Date(now.getTime()));
        token.set_exp(new Date(now.getTime() + 1000 * 60 * 60 * 24));

        String sql = "INSERT INTO " + _db_prefix + "auth_tokens (`jti`, `sub`, `iat`, `exp`)" +
                "VALUES (UUID_TO_BIN(?), ?, ?, ?)";
        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, token.get_jti());
            prep.setString(2, token.get_sub());
            prep.setTimestamp(3, new Timestamp(token.get_iat().getTime()));
            prep.setTimestamp(4, new Timestamp(token.get_exp().getTime()));
            prep.executeUpdate();

            return token;
        }
        catch (Exception e) { _logger.log(Level.WARNING, e.getMessage() + " -- " + sql); }

        return null;
    }

    private Timestamp GetDbTimestamp()
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
}
