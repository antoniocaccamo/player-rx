package me.antoniocaccamo.player.rx.event.resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.antoniocaccamo.player.rx.model.resource.Resource;

/**
 * @author antoniocaccamo  on 16/11/2020
 */


public class SelecteResourceEvent extends ResourceEvent{

    public SelecteResourceEvent(Resource resource) {
        super(resource);
    }


}
