package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import step.learning.services.culture.IResourceProvider;
import step.learning.services.culture.StringResourceProvider;
import step.learning.services.db.IDbProvider;
import step.learning.services.db.PlanetDbProvider;
import step.learning.services.formparse.IFormParsService;
import step.learning.services.formparse.MixedFormParsService;
import step.learning.services.hash.IHashService;
import step.learning.services.hash.Md5HashService;
import step.learning.services.hash.Sha1HashService;
import step.learning.services.kdf.DigestHashKdfService;
import step.learning.services.kdf.IKdfService;
import step.learning.services.random.*;

public class ServicesModule extends AbstractModule
{
    @Override
    protected void configure()
    {
        bind(IHashService.class).annotatedWith(Names.named("Digest-hash")).to(Md5HashService.class);
        bind(IHashService.class).annotatedWith(Names.named("Signature-hash")).to(Sha1HashService.class);
        bind(IResourceProvider.class).to(StringResourceProvider.class);
        bind(IFormParsService.class).to(MixedFormParsService.class);
        bind(IDbProvider.class).to(PlanetDbProvider.class);
        bind(IKdfService.class).to(DigestHashKdfService.class);
        bind(String.class).annotatedWith(Names.named("db-prefix")).toInstance("java_web");
    }

    private IRandomServices random_service ;

    @Provides
    private IRandomServices InjectRandomService()
    {
        if( random_service == null )
        {
            random_service = new RandomServiceV1() ;

            random_service.Seed( "initial" ) ;
        }
        return random_service ;
    }
}
