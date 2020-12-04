package me.antoniocaccamo.player.rx.event.resource;

import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;

/**
 * @author antoniocaccamo  on 16/11/2020
 */
public class ModifyMediaEvent extends ResourceEvent{

    private Sequence sequence;

    public ModifyMediaEvent(Sequence sequence, Resource resource) {
        super(resource);
        this.sequence = sequence;
    }


}
