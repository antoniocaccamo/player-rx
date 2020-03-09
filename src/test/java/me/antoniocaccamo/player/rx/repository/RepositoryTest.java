package me.antoniocaccamo.player.rx.repository;

import io.micronaut.test.annotation.MicronautTest;
//import lombok.extern.slf4j.Slf4j;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.service.ResourceService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.Duration;

@MicronautTest
//@Slf4j
public class RepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(RepositoryTest.class);

    @Inject
    private ResourceRepository resourceRepository;

    @Inject
    private MediaRepository mediaRepository;

    @Inject SequenceRepository sequenceRepository;

    @Inject
    private ResourceService resourceService;


    @Test @Order(1)
    public void saveAndReadResource() {

        LocalResource blackResource = LocalResource.builder()
                .withType(Resource.TYPE.BLACK)
                .build()
        ;

        LocalResource hiddenResource = LocalResource.builder()
                .withType(Resource.TYPE.HIDDEN)
                .build()
        ;

        LocalResource watchResource = LocalResource.builder()
                .withType(Resource.TYPE.WATCH)
                .build()
        ;

        LocalResource weatherResource = LocalResource.builder()
                .withType(Resource.TYPE.WEATHER)
                .build()
        ;

        LocalResource logo = LocalResource.builder()
                .withPath("images/logo.jpg")
                .withType(Resource.TYPE.PHOTO)
                .build()
        ;

        Observable.just(blackResource, hiddenResource, watchResource, weatherResource, logo)
                .subscribe(new Observer<LocalResource>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        logger.info("begin saving localResources");
                    }

                    @Override
                    public void onNext(LocalResource localResource) {
                        logger.info("saving localResource : {}", localResource);
                        LocalResource lr = resourceRepository.save(localResource);
                        logger.info("saved localResource : {}", lr);
                    }

                    @Override
                    public void onError(Throwable e) {
                        logger.error("error occurred", e);
                    }

                    @Override
                    public void onComplete() {
                        logger.info("saved  localResources");
                    }
                });

        resourceRepository.findAll()
                .forEach(resource -> logger.info("resource read  : {}", resource));


        Resource resource;

        resource  = resourceRepository.findByType(Resource.TYPE.WATCH);

        Media watchMedia = Media.builder()
                .duration(Duration.ofSeconds(10))
                .resource(resource)
                .build();

        resource = resourceRepository.findByType(Resource.TYPE.WEATHER);
        Media weatherMedia = Media.builder()
                .duration(Duration.ofSeconds(10))
                .resource(resource)
                .build();

        Observable.just(watchMedia, weatherMedia)
                .subscribe(media -> mediaRepository.save(media));

        mediaRepository.findAll()
                .forEach( media -> logger.info("media read : {}", media))
        ;


    }
}
