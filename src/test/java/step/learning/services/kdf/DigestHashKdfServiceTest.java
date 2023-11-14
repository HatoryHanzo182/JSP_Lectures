package step.learning.services.kdf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import step.learning.services.hash.IHashService;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DigestHashKdfServiceTest
{
    private IKdfService _kdf_service;
    private IHashService _hash_service_mock;

    @BeforeEach
    public void setUp()
    {
        _hash_service_mock = mock(IHashService.class);
        _kdf_service = new DigestHashKdfService(_hash_service_mock);
    }

    @Test
    public void testGetDerivedKey()
    {
        String expected_hash = "expectedHashValue"; // Set the expected hash value.

        when(_hash_service_mock.Hash(anyString())).thenReturn(expected_hash); // Set the behavior of the mock: when Hash is called, return the expected value.

        String actual_hash = _kdf_service.GetDerivedKey("password", "salt");  // Call the GetDerivedKey method and check that the expected value is returned.

        verify(_hash_service_mock).Hash("saltpasswordsalt");  // Check that the Hash method was called with the correct arguments.
        assertEquals(expected_hash, actual_hash);  // Check that the result matches the expected value.
    }
}