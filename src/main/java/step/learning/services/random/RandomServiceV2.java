package step.learning.IOC.Services.Random;

import java.util.Random;

public class RandomServiceV2 implements IRandomServices
{
    private final Random _random;
    private final char[] _HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public RandomServiceV2()
    {
        _random = new Random();
    }

    @Override
    public void Seed(String iv)
    {
        _random.setSeed(iv.length());
    }

    @Override
    public String RandomHex(int char_length)
    {
        char[] result = new char[char_length];

        for(int i = 0; i < char_length; i++)
        {
            int index = _random.nextInt(_HEX_CHARS.length);

            result[i] = _HEX_CHARS[index];
        }
        return new String(result);
    }
}
