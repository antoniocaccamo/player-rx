package me.antoniocaccamo.player.rx.helper;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Observable;
import io.reactivex.Observer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.transaction.Transactional;

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
import me.antoniocaccamo.player.rx.service.impl.LegacyServiceImpl;

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

        sequence = sequenceRepository.findByName(Constants.DefaultSequenceName)
                .orElseGet(this::createDefaultSequence)

        ;

        return sequence;
    }

    @Transactional
    private Sequence createDefaultSequence() {

        log.warn("creating default sequence ..");

        Sequence sequence = Constants.DEFAULT_SEQUENCE();
        sequence.getMedias()
                .stream()
                .forEach(media -> {
                    Resource resource = media.getResource();
                    resourceRepository.save(resource);
                    //media.setResource(resource);
                    mediaRepository.save(media);
                });
        sequenceRepository.save(sequence);
        ObjectMapper mapper = new ObjectMapper();
        log.info("default sequence saved : {} " , sequence);


        return sequence;

    }
}
