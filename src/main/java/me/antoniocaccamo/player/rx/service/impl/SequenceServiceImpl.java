package me.antoniocaccamo.player.rx.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import me.antoniocaccamo.player.rx.service.SequenceService;

import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.nio.file.Path;

@Singleton @Slf4j
public class SequenceServiceImpl implements SequenceService {
    @Override
    public Sequence read(String path) {
        return null;
    }

    @Override
    public void save(Sequence sequence, Path path) {

    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
    }
}
