package me.antoniocaccamo.player.rx.repository;

import io.micronaut.data.annotation.*;
import io.micronaut.data.repository.CrudRepository;
import me.antoniocaccamo.player.rx.model.resource.Resource;

@Repository
interface ResourceRepository extends CrudRepository<Resource, Long> {
    // Resource find(String title);
}
