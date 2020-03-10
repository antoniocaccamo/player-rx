package me.antoniocaccamo.player.rx.repository;

import io.micronaut.data.annotation.*;
import io.micronaut.data.repository.CrudRepository;
import me.antoniocaccamo.player.rx.model.resource.Resource;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Long> {
    Resource findByType(Resource.TYPE type);
}
