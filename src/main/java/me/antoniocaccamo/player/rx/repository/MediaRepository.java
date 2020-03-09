package me.antoniocaccamo.player.rx.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import me.antoniocaccamo.player.rx.model.sequence.Media;

@Repository
interface MediaRepository extends CrudRepository<Media, Long> {
    // Resource find(String title);
}
