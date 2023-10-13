package step.learning.services.culture;

import com.google.inject.Singleton;

@Singleton
public class StringResourceProvider implements IResourceProvider
{
    private String _default_culture;

    @Override
    public String GetString(String name, String culture)
    {
        switch (name)
        {
            case "signup_login_too_short":
                switch (culture)
                {
                    default:
                        return "Short login!";
                }
            case "signup_login_empty":
                switch (culture)
                {
                    default:
                        return "Login cannot be empty!";
                }
            case "signup_login_pattern_mismatch":
                switch (culture)
                {
                    default:
                        return "Unallowed characters!";
                }
        }
        return null;
    }

    @Override
    public String GetString(String name) { return GetString(name, _default_culture); }

    @Override
    public void SetCulture(String culture) { _default_culture = culture; }

    @Override
    public String GetCulture() { return _default_culture; }
}
