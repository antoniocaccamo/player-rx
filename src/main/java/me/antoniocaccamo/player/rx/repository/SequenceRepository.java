package me.antoniocaccamo.player.rx.repository;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;

import java.util.Optional;

@Repository
public interface SequenceRepository extends CrudRepository<Sequence, Long> {

    @NonNull
    @Override
    Optional<Sequence> findById(@NonNull  Long aLong);

    Optional<Sequence> findByName(String name);
}
