package step.learning.ioc;

import com.google.inject.AbstractModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

public class LoggingModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        try(InputStream properties = this.getClass().getClassLoader().getResourceAsStream("logging.properties"))
        {
            LogManager manager = LogManager.getLogManager();

            manager.reset();
            manager.readConfiguration(properties);
        }
        catch (IOException ex) { System.err.println("Logger config error: " + ex.getMessage()); }
    }
}