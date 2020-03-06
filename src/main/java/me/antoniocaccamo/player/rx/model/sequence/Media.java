package me.antoniocaccamo.player.rx.model.sequence;

import lombok.*;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.resource.AbstractResource;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.yaml.snakeyaml.Yaml;

import java.time.Duration;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media implements Cloneable{

    private Model.Location location;
    private Model.Type         type;
    private String             path;
    private Duration       duration;


    private AbstractResource resource;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("location", location)
                .append("type", type)
                .append("path", path)
                .append("duration", duration)
                .append("resource", resource)
                .toString();
    }
}
