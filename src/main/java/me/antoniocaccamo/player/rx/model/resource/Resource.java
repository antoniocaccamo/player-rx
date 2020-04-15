package me.antoniocaccamo.player.rx.model.resource;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import me.antoniocaccamo.player.rx.config.Constants;
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




    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="RESOURCE_SEQ")
    @SequenceGenerator(name="RESOURCE_SEQ", sequenceName="RESOURCE_SEQ", allocationSize=1)
    protected Long id;

    @Column
    protected Constants.Resource.Type type;

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

    public Constants.Resource.Type getType() {
        return type;
    }

    public void setType(Constants.Resource.Type type) {
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

    @Transient @JsonIgnore
    public abstract Path getLocalPath() ;

    @Transient
    public boolean isVideo(){
        return Constants.Resource.Type.VIDEO.equals(getType());
    }

    @Transient
    public abstract boolean isLocal();

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("type", type)
                .append("path", path)
                .append("duration", duration)
                .toString();
    }

    @Transient @JsonIgnore
    public abstract boolean needsTrancode();
}
