package me.antoniocaccamo.player.rx.helper;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import me.antoniocaccamo.player.rx.repository.*;
import me.antoniocaccamo.player.rx.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author antoniocaccamo on 10/03/2020
 */
@Singleton
public class DBInitHelper {

    private static final Logger logger = LoggerFactory.getLogger(DBInitHelper.class);
    private static final String SEQUENCE_NAME = "test sequence";

    @Inject
    private ResourceRepository resourceRepository;

    @Inject
    private MediaRepository mediaRepository;

     @Inject
    private SequenceRepository sequenceRepository;

    @Inject
    private ResourceService resourceService;

    public DBInitHelper() {
    }

    public Sequence  getDefaultSquence(){

        Sequence sequence = null;

        sequence = sequenceRepository.findByName(SEQUENCE_NAME)
                .orElseGet(this::createDefaultSequence)
        ;

        return sequence;
    }


    private Sequence createDefaultSequence() {

        logger.warn("creating default sequence ..");

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

        resource  = resourceRepository.findByType(Resource.TYPE.PHOTO);

        Media photohMedia = Media.builder()
                .duration(Duration.ofSeconds(5))
                .resource(resource)
                .build();

        resource  = resourceRepository.findByType(Resource.TYPE.WATCH);

        Media watchMedia = Media.builder()
                .duration(Duration.ofSeconds(5))
                .resource(resource)
                .build();

        resource = resourceRepository.findByType(Resource.TYPE.WEATHER);
        Media weatherMedia = Media.builder()
                .duration(Duration.ofSeconds(10))
                .resource(resource)
                .build();

        Observable.just(photohMedia, watchMedia, weatherMedia)
                .subscribe(media -> logger.info("media saved : {}", mediaRepository.save(media)));

        mediaRepository.findAll()
                .forEach( media -> logger.info("media read : {}", media))
        ;


        Sequence sequence = Sequence.builder()
                .name(SEQUENCE_NAME)
                .location(Model.Location.LOCAL)
                .medias(Arrays.asList(photohMedia, watchMedia, weatherMedia))
                .build();

        sequence = sequenceRepository.save(sequence);

        logger.info("sequence saved : {}", sequence);

        return sequence;

    }
}
