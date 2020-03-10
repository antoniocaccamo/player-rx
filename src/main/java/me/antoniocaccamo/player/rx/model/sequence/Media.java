package me.antoniocaccamo.player.rx.model.sequence;

import lombok.*;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.time.Duration;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "MEDIA")
public class Media implements Cloneable{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="MEDIA_SEQ")
    @SequenceGenerator(name="MEDIA_SEQ", sequenceName="MEDIA_SEQ", allocationSize=1)
    protected Long id;

    @Transient
    private Model.Location location;

    @Transient
    private Model.Type         type;

    @Transient
    private String             path;

    @Column
    private Duration       duration;

    @ManyToOne
    @JoinColumn(name="RESOURCE_ID")
    private Resource resource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Model.Location getLocation() {
        return location;
    }

    public void setLocation(Model.Location location) {
        this.location = location;
    }

    public Model.Type getType() {
        return type;
    }

    public void setType(Model.Type type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("location", location)
                .append("type", type)
                .append("path", path)
                .append("duration", duration)
                .append("resource", resource)
                .toString();
    }
}
