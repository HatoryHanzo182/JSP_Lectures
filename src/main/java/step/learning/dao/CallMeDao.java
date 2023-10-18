package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.dto.entities.CallMe;
import step.learning.services.db.IDbProvider;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
