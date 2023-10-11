package step.learning.IOC.Services.Random;

public interface IRandomServices
{
    void Seed(String iv);
    String RandomHex(int char_length);
}