package me.antoniocaccamo.player.rx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import me.antoniocaccamo.player.rx.repository.SequenceRepository;
import me.antoniocaccamo.player.rx.service.SequenceService;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Optional;

import me.antoniocaccamo.player.rx.config.Constants;

@Singleton @Slf4j
public class SequenceServiceImpl implements SequenceService {

    private final ObjectMapper mapper = new ObjectMapper();
    private Sequence dummy = Constants.DEFAULT_SEQUENCE();

    @Inject
    private SequenceRepository sequenceRepository;

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
    public void save(Sequence sequence, Path path) {

    }

    @Override
    public Optional<Sequence> getSequenceByName(String sequenceName) {
        return sequenceRepository.findByName(sequenceName);
    }

    @Override
    public Sequence dummy() {
        return dummy;
    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
    }
}
