package me.antoniocaccamo.player.rx.model.sequence;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @Slf4j
@Entity
@Table(name = "SEQUENCE")
public class Sequence implements Cloneable{

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQUENCE_SEQ")
    @SequenceGenerator(name="SEQUENCE_SEQ", sequenceName="SEQUENCE_SEQ", allocationSize=1)
    protected Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Transient
    private Model.Location location;

    @OneToMany
    @JoinTable(
            name = "SEQUENCE_MEDIA",
            joinColumns = { @JoinColumn(name = "SEQUENCE_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {@JoinColumn(name = "MEDIA_ID", referencedColumnName = "ID")}
    )
    private List<Media> medias;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Model.Location getLocation() {
        return location;
    }

    public void setLocation(Model.Location location) {
        this.location = location;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("name", name)
                .append("location", location)
                .append("medias", medias)
                .toString();
    }

    @Override
    public Sequence clone() {
        Sequence sequence = null;
        try {
             sequence = this.clone();
            return sequence;
        } catch (Exception e) {
            log.error("error occurred", e);
            sequence = Sequence.builder()
                    .name("errorrr")
                    .medias(Collections.emptyList())
                    .build();
        } finally {
            return sequence;
        }
    }
}
