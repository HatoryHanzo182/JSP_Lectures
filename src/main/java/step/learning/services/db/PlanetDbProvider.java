package step.learning.services.db;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

@Singleton
public class PlanetDbProvider implements IDbProvider
{
    private Connection _connection;

    @Override
    public Connection GetConnection()
    {
        JsonObject db_config = null;

        if (_connection == null)
        {
            try (Reader reader = new InputStreamReader(Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream("db_config.json"))))
            {
                db_config = JsonParser.parseReader(reader).getAsJsonObject();
            }
            catch (IOException ex) { throw new RuntimeException(ex); }
            catch (NullPointerException ex) { throw new RuntimeException("Resource not found"); }

            try
            {
                JsonObject planet_config = db_config.get("DataProviders").getAsJsonObject().get("PlanetScale").getAsJsonObject();
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

                _connection = DriverManager.getConnection(planet_config.get("url").getAsString(),
                        planet_config.get("user").getAsString(), planet_config.get("password").getAsString());
            }
            catch (SQLException ex) { throw new RuntimeException(ex); }
        }
        return _connection;
    }
}
