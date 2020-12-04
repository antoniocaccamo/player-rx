package me.antoniocaccamo.player.rx.event.resource;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.antoniocaccamo.player.rx.event.PlayerEvent;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author antoniocaccamo  on 16/11/2020
 */

public abstract class ResourceEvent extends PlayerEvent {

    @Getter
    protected final Resource resource;

    protected ResourceEvent(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("resource", resource)
                .toString()
                ;
    }
}
