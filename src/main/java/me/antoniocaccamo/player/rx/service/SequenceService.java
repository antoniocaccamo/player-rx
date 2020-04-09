package me.antoniocaccamo.player.rx.service;

import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;

import java.nio.file.Path;
import java.util.Optional;

public interface SequenceService {

    Sequence read( Path path);

    Sequence read(Model.Location location, Path path);

    void save(Sequence sequence, Path path);

    Optional<Sequence> getSequenceByName(String seqeunceName);

    Sequence dummy();
}
