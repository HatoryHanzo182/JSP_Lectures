package step.learning.dao;

import com.google.gson.JsonParser;
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
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class AuthTokenDao extends DaoBase
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;
    private final Logger _logger;
    private final UserDao _user_dao;

    @Inject
    public AuthTokenDao(IDbProvider db_provider, @Named("db-prefix") String db_prefix, Logger logger, UserDao user_dao)
    {
        super(logger, db_provider);

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

        AuthToken existing_token = GetActiveTokenForUser(user.GetId());

        if (existing_token != null)
            return existing_token;

        AuthToken token = new AuthToken();

        token.SetJti(UUID.randomUUID().toString());
        token.SetSub(user.GetId());

        Timestamp now = GetDbTimestamp();

        if(now == null)
            return null;

        token.SetIat(new Date(now.getTime()));
        token.SetExp(new Date(now.getTime() + 1000 * 60 * 60 * 24));

        String sql = "INSERT INTO " + _db_prefix + "auth_tokens (`jti`, `sub`, `iat`, `exp`)" +
                "VALUES (UUID_TO_BIN(?), ?, ?, ?)";
        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, token.GetJti());
            prep.setString(2, token.GetSub());
            prep.setTimestamp(3, new Timestamp(token.GetIat().getTime()));
            prep.setTimestamp(4, new Timestamp(token.GetExp().getTime()));
            prep.executeUpdate();

            return token;
        }
        catch (Exception e) { _logger.log(Level.WARNING, e.getMessage() + " -- " + sql); }

        return null;
    }

    private AuthToken GetActiveTokenForUser(String userId)
    {
        String sql = "SELECT BIN_TO_UUID(a.`jti`) AS jti, a.`sub`, a.`iat`, a.`exp`, u.`login` AS nik FROM " + _db_prefix +
                "auth_tokens a JOIN " + _db_prefix + "users u ON a.sub = u.id " +
                "WHERE a.`jti` = ? AND a.`exp` > CURRENT_TIMESTAMP";

        try (PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, userId);

            ResultSet resultSet = prep.executeQuery();

            if (resultSet.next())
                return new AuthToken(resultSet);
        }
        catch (Exception e) { _logger.log(Level.WARNING, e.getMessage() + " -- " + sql); }

        return null;
    }

    public AuthToken GetTokenByBearer(String bearer_content)
    {
        String jti;

        try
        {
            jti = JsonParser.parseString(new String(Base64.getUrlDecoder().decode(bearer_content))).getAsJsonObject().get("_jti").getAsString();
        }
        catch(Exception ex) { return null; }

        String sql = "SELECT BIN_TO_UUID(a.`jti`) AS jti, a.`sub`, a.`iat`, a.`exp`, u.`login` AS nik FROM " + _db_prefix +
                "auth_tokens a JOIN " + _db_prefix + "users u ON a.sub = u.id " +
                "WHERE a.`jti` = UUID_TO_BIN(?) AND a.`exp` > CURRENT_TIMESTAMP";

        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, jti);

            ResultSet resultSet = prep.executeQuery();

            if (resultSet.next())
                return new AuthToken(resultSet);

        }
        catch(Exception e) { _logger.log(Level.WARNING, e.getMessage() + " -- " + sql); }

        return null;
    }

    public void RenewToken(AuthToken token)
    {
        String sql = "UPDATE " + _db_prefix + "auth_tokens SET `exp` = DATE_ADD(CURRENT_TIMESTAMP, INTERVAL 12 HOUR) WHERE `jti` = UUID_TO_BIN(?)";

        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, token.GetJti());
            prep.executeUpdate();

            Date new_exp = new Date(token.GetExp().getTime() + 1000L * 60 * 60 * 12);
            token.SetExp(new_exp);
        }
        catch(Exception e) { _logger.log(Level.WARNING, e.getMessage() + " -- " + sql); }
    }
}
