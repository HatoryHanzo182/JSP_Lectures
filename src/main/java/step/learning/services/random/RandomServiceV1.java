package step.learning.IOC.Services.Random;

import java.util.Random;

public class RandomServiceV1 implements IRandomServices
{
    private final Random _random;
    private final char[] _HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public RandomServiceV1()
    {
        _random = new Random();
    }

    @Override
    public void Seed(String iv) { _random.setSeed(iv.length()); }

    @Override
    public String RandomHex(int char_length)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < char_length; i++)
        {
            int index = _random.nextInt(0, _HEX_CHARS.length);
            sb.append(_HEX_CHARS[index]);
        }
        return sb.toString();
    }
}
