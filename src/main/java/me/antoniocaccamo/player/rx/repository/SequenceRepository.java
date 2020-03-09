package me.antoniocaccamo.player.rx.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;

@Repository
interface SequenceRepository extends CrudRepository<Sequence, Long> {
    // Resource find(String title);
}
