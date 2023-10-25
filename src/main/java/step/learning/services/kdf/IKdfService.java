package step.learning.services.kdf;

public interface IKdfService
{
    String GetDerivedKey(String password, String salt);
}
