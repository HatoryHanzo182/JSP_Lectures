package step.learning.services.kdf;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import step.learning.services.hash.IHashService;

@Singleton
public class DigestHashKdfService implements IKdfService
{
    private final IHashService _hash_service;

    @Inject
    public DigestHashKdfService(@Named("Digest-hash") IHashService hash_service)
    {
        _hash_service = hash_service;
    }

    @Override
    public String GetDerivedKey(String password, String salt)
    {
        return _hash_service.Hash(salt + password + salt);
    }
}
