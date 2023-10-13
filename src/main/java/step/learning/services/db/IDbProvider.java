package step.learning.services.db;

import java.sql.Connection;

public interface IDbProvider
{
    Connection GetConnection();
}
