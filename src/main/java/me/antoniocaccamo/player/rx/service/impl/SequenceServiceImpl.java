package me.antoniocaccamo.player.rx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import me.antoniocaccamo.player.rx.repository.SequenceRepository;
import me.antoniocaccamo.player.rx.service.SequenceService;

import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

@Singleton @Slf4j
public class SequenceServiceImpl implements SequenceService {

    private final ObjectMapper mapper = new ObjectMapper();
    private Sequence dummy = Constants.DEFAULT_SEQUENCE();

    private final ConcurrentMap<String, Optional<Sequence>> sequenceMap= new ConcurrentSkipListMap<>();

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
        Optional<Sequence> sequence = null;
        if ( ! sequenceMap.containsKey(sequenceName)) {
            log.info("loading sequence : {}", sequenceName);
            sequenceMap.put(sequenceName, sequenceRepository.findByName(sequenceName));
        }
        sequence = sequenceMap.get(sequenceName);
        return sequence;
    }



    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
    }
}
