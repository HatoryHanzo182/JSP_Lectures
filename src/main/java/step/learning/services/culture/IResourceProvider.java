package step.learning.services.culture;

public interface IResourceProvider
{
    String GetString(String name, String culture);

    String GetString(String name);

    void SetCulture(String culture);

    String GetCulture();
}
