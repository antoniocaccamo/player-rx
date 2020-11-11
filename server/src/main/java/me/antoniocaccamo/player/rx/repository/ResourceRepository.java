package me.antoniocaccamo.player.rx.repository;

// import io.micronaut.data.annotation.*;
// import io.micronaut.data.repository.CrudRepository;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.config.Constants;

//@Repository
public interface ResourceRepository //extends CrudRepository<Resource, Long>
{
    Resource findByType(Constants.Resource.Type type);
}
