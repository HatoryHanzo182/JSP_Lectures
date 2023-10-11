package step.learning.IOC.Services.Hash;

import com.google.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class Md5HashService implements IHashService
{
    private final char[] _HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Override
    public String Hash(String input)
    {
        if (input == null)
            input = "";

        try
        {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            char[] chars = new char[32];
            int i = 0;

            for(int b : digest.digest(input.getBytes(StandardCharsets.UTF_8)))
            {
                chars[i] = _HEX_CHARS[(b & 0xF0) >> 4];
                chars[i + 1] = _HEX_CHARS[b & 0x0F];
                i += 2;
            }

            return new String(chars);
        }
        catch (NoSuchAlgorithmException e) { throw new RuntimeException(e); }
    }
}