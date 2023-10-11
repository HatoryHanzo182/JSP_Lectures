package step.learning.ioc;

import com.google.inject.AbstractModule;

public class ServicesModule extends AbstractModule
{
    @Override
    protected void configure()
    {
//        bind( HashService.class )
//                .annotatedWith( Names.named( "Digest-hash" ) )
//                .to( Md5HashService.class ) ;
//        bind( HashService.class )
//                .annotatedWith( Names.named( "Signature-hash" ) )
//                .to( Sha1HashService.class ) ;
    }
}
