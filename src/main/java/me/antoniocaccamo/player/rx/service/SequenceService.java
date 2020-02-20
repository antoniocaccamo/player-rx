package me.antoniocaccamo.player.rx.service;

import me.antoniocaccamo.player.rx.model.sequence.Sequence;

import java.nio.file.Path;

public interface SequenceService {

    Sequence read(String path);

    void save(Sequence sequence, Path path);


}