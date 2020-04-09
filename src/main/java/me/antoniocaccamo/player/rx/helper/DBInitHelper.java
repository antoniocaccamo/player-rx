package me.antoniocaccamo.player.rx.helper;

import java.time.Duration;
import java.util.Arrays;
import io.reactivex.Observable;
import io.reactivex.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.Disposable;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import me.antoniocaccamo.player.rx.repository.MediaRepository;
import me.antoniocaccamo.player.rx.repository.ResourceRepository;
import me.antoniocaccamo.player.rx.repository.SequenceRepository;
import me.antoniocaccamo.player.rx.service.ResourceService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author antoniocaccamo on 10/03/2020
 */
@Singleton @Slf4j
public class DBInitHelper {

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

        log.warn("creating default sequence ..");

        LocalResource blackResource = LocalResource.builder()
                .withType(Constants.Resource.Type.BLACK)
                .build()
                ;

        LocalResource hiddenResource = LocalResource.builder()
                .withType(Constants.Resource.Type.HIDDEN)
                .build()
                ;

        LocalResource watchResource = LocalResource.builder()
                .withType(Constants.Resource.Type.WATCH)
                .build()
                ;

        LocalResource weatherResource = LocalResource.builder()
                .withType(Constants.Resource.Type.WEATHER)
                .build()
                ;

        LocalResource logo = LocalResource.builder()
                .withPath("images/logo.jpg")
                .withType(Constants.Resource.Type.PHOTO)
                .build()
                ;

        Observable.just(blackResource, hiddenResource, watchResource, weatherResource, logo)
                .subscribe(new Observer<LocalResource>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        log.info("begin saving localResources");
                    }

                    @Override
                    public void onNext(LocalResource localResource) {
                        log.info("saving localResource : {}", localResource);
                        LocalResource lr = resourceRepository.save(localResource);
                        log.info("saved localResource : {}", lr);
                    }

                    @Override
                    public void onError(Throwable e) {
                        log.error("error occurred", e);
                    }

                    @Override
                    public void onComplete() {
                        log.info("saved  localResources");
                    }
                });

        resourceRepository.findAll()
                .forEach(resource -> log.info("resource read  : {}", resource));


        Resource resource;

        resource  = resourceRepository.findByType(Constants.Resource.Type.PHOTO);

        Media photohMedia = Media.builder()
                .duration(Duration.ofSeconds(5))
                .resource(resource)
                .build();

        resource  = resourceRepository.findByType(Constants.Resource.Type.WATCH);

        Media watchMedia = Media.builder()
                .duration(Duration.ofSeconds(5))
                .resource(resource)
                .build();

        resource = resourceRepository.findByType(Constants.Resource.Type.WEATHER);
        Media weatherMedia = Media.builder()
                .duration(Duration.ofSeconds(10))
                .resource(resource)
                .build();

        Observable.just(photohMedia, watchMedia, weatherMedia)
                .subscribe(media -> log.info("media saved : {}", mediaRepository.save(media)));

        mediaRepository.findAll()
                .forEach( media -> log.info("media read : {}", media))
        ;


        Sequence sequence = Sequence.builder()
                .name(SEQUENCE_NAME)
                .location(Model.Location.LOCAL)
                .medias(Arrays.asList(photohMedia, watchMedia, weatherMedia))
                .build();

        sequence = sequenceRepository.save(sequence);

        log.info("sequence saved : {}", sequence);

        return sequence;

    }
}
