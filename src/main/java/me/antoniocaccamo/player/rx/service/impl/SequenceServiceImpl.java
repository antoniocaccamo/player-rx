package me.antoniocaccamo.player.rx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import me.antoniocaccamo.player.rx.repository.MediaRepository;
import me.antoniocaccamo.player.rx.repository.ResourceRepository;
import me.antoniocaccamo.player.rx.repository.SequenceRepository;
import me.antoniocaccamo.player.rx.service.SequenceService;
import me.antoniocaccamo.player.rx.service.TranscodeService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutionException;

@Singleton @Slf4j
public class SequenceServiceImpl implements SequenceService {

    private final ObjectMapper mapper = new ObjectMapper();
    private Sequence dummy = Constants.DEFAULT_SEQUENCE();

    private final ConcurrentMap<String, Optional<Sequence>> sequenceMap= new ConcurrentSkipListMap<>();

    private Cache<String, Sequence> sequenceCache = null;

    @Inject
    private SequenceRepository sequenceRepository;

    @Inject
    private ResourceRepository resourceRepository;

    @Inject
    private MediaRepository mediaRepository;

    @Inject
    private TranscodeService transcodeService;

    @PostConstruct
    public void postConstuct(){
        sequenceCache = CacheBuilder.newBuilder()
                .recordStats()
                .build()
        ;


//        sequenceCache.putAll(
//                Observable.fromIterable(sequenceRepository.findAll())
//                        .toMap( sq -> sq.getName())
//                        .blockingGet()
//        );

    }

    @Override
    public Sequence read( Path path){
        return read(Model.Location.LOCAL, path);
    }


    @Override
    public Sequence read(Model.Location location, Path path) {

        log.info("reading sequence from location {} and  path : {}", location, path);

        dummy.clone();
        dummy.setName(path.toFile().getName());

        try {
            log.info("dummy : {}", mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dummy) );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return dummy
                ;
    }

    @Override
    public Sequence save(Sequence sequence, Path path) {

        sequence.getMedias()
                .stream()
                .forEach(media -> {
                    Resource resource = media.getResource();
                    resourceRepository.save(resource);
                    //media.setResource(resource);
                    mediaRepository.save(media);
                });
        sequenceRepository.save(sequence);
        sequenceCache.put(sequence.getName(), sequence);
        return sequence;
    }

    @Override
    public Optional<Sequence> getSequenceByName(String sequenceName) {
        Optional<Sequence> sequence = Optional.empty();
//        if ( ! sequenceMap.containsKey(sequenceName)) {
//            log.info("loading sequence : {}", sequenceName);
//            sequenceMap.put(sequenceName, sequenceRepository.findByName(sequenceName));
//        }
//        sequence = sequenceMap.get(sequenceName);


        log.warn("sequence by name : {}", sequenceName);
        sequence = Optional.ofNullable(sequenceCache.getIfPresent(sequenceName));
        sequence.ifPresent(sq -> sq.getMedias()
                .stream()
                .filter(media -> media.getResource().isVideo() &&  media.getResource().needsTrancode())
                .forEach(media -> transcodeService.transcode(media.getResource()))

        );

        return sequence;
    }

    @Override
    public Collection<Sequence> getLoadedSequences() {
        return sequenceCache.asMap().values();
    }


    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
    }
}
