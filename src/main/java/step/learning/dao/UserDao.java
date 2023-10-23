package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.models.SignupFormModel;
import step.learning.services.db.IDbProvider;
import step.learning.services.hash.IHashService;
import step.learning.services.random.IRandomServices;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class UserDao
{
    private final IDbProvider _db_provider;
    private final String _db_prefix;
    private final Logger _logger;
    private final IRandomServices _random_services;
    private final IHashService _hash_service;

    @Inject
    public UserDao(IDbProvider db_provider, @Named("db-prefix") String db_prefix, Logger logger,
                   IRandomServices random_services, @Named("Digest-hash") IHashService hash_service)
    {
        _db_provider = db_provider;
        _db_prefix = db_prefix;
        _logger = logger;
        _random_services = random_services;
        _hash_service = hash_service;
    }

    public boolean Install()
    {
        String sql = "CREATE TABLE " + _db_prefix + "users(" +
                "`id` BIGINT UNSIGNED PRIMARY KEY DEFAULT (UUID_SHORT())," +
                "`login` VARCHAR(64) NOT NULL UNIQUE," +
                "`real_name` VARCHAR(64) NOT NULL," +
                "`email` VARCHAR(96) NOT NULL," +
                "`salt` CHAR(16) NOT NULL," +
                "`pass_dk` CHAR(32) NOT NULL," +
                "`birthday` DATE NULL," +
                "`avatar_url` VARCHAR(96) NULL," +
                "`register_moment` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                "`delete_moment` DATETIME NULL" +
                ") ENGINE = InnoDB, DEFAULT CHARSET = utf8mb4 COLLATE utf8mb4_unicode_ci";

        try(Statement statement = _db_provider.GetConnection().createStatement())
        {
            statement.executeUpdate(sql);
            return true;
        }
        catch (Exception ex) { _logger.log(Level.WARNING, ex.getMessage() + " - - " + sql); }
        return false;
    }

    public boolean AddFromForm(SignupFormModel model)
    {
        String sql = "INSERT INTO " + _db_prefix + "users (" +
                "`login`, `real_name`, `email`,`salt`, `pass_dk`, `birthday`, `avatar_url`)" +
                " VALUES(?,?,?,?,?,?,?)";
        String salt = _random_services.RandomHex(16);
        String pass_dk = _hash_service.Hash(salt + model.GetPassword() + salt);

        try(PreparedStatement prep = _db_provider.GetConnection().prepareStatement(sql))
        {
            prep.setString(1, model.GetLogin());
            prep.setString(2, model.GetName());
            prep.setString(3, model.GetEmail());
            prep.setString(4, salt);
            prep.setString(5, pass_dk);

            java.util.Date date = model.GetBirthdate();

            prep.setDate(6, date == null ? null : new java.sql.Date(date.getTime()));
            prep.setString(7, model.GetAvatar());
            prep.executeUpdate();
            return true;
        }
        catch (Exception ex) { _logger.log(Level.WARNING, ex.getMessage() + " - - " + sql); }

        return false;
    }
}
