package me.antoniocaccamo.player.rx.model.resource;


import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="RESOURCE_SEQ")
    @SequenceGenerator(name="RESOURCE_SEQ", sequenceName="RESOURCE_SEQ", allocationSize=1)
    protected Long id;

    @Column
    protected me.antoniocaccamo.player.rx.config.Constants.Resource.Type type;

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

    public me.antoniocaccamo.player.rx.config.Constants.Resource.Type getType() {
        return type;
    }

    public void setType(me.antoniocaccamo.player.rx.config.Constants.Resource.Type type) {
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

    //@Transient @JsonIgnore
    //public abstract Path getHLSPath() ;

    @Transient
    public boolean isVideo(){
        return me.antoniocaccamo.player.rx.config.Constants.Resource.Type.VIDEO.equals(getType());
    }

    @Transient
    public abstract boolean isLocal();

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("type", type)
                .append("path", getPath())
                .append("duration", getDuration())
                .append("hash", getHash())
                .append("localpath", getLocalPath())
                .toString();
    }

    @Transient @JsonIgnore
    public abstract boolean needsTrancode();

    @Transient @JsonIgnore
    public abstract String getHash();
}
