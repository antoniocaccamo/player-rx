package me.antoniocaccamo.player.rx.model.resource;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.nio.file.Path;
import java.time.Duration;

/**
 * @author antoniocaccamo on 18/02/2020
 */
@Getter @Setter
@Inheritance @DiscriminatorColumn(name = "LOCATION")
@Entity
@Table(name = "RESOURCE")

public abstract class Resource {


    public enum TYPE {
        BLACK,
        HIDDEN,
        WATCH,
        WEATHER,
        PHOTO,
        VIDEO
    }

    public interface LOCATION {
        String LOCAL   = "LOCAL";
        String REMOTE = "REMOTE";
    }

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="RESOURCE_SEQ")
    @SequenceGenerator(name="RESOURCE_SEQ", sequenceName="RESOURCE_SEQ", allocationSize=1)
    protected Long id;

    @Column
    protected TYPE type;

    //@Column
    //protected LOCATION location;

    @Column
    protected String path;

    @Column
    protected Duration duration;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
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

    @Transient
    public abstract Path getLocalPath() ;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("type", type)
                .append("path", path)
                .append("duration", duration)
                .toString();
    }
}
