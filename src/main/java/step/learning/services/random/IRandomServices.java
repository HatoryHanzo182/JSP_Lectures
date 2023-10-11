package step.learning.services.random;

public interface IRandomServices
{
    void Seed(String iv);
    String RandomHex(int char_length);
}