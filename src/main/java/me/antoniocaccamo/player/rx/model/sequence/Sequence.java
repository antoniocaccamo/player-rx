package me.antoniocaccamo.player.rx.model.sequence;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder @Slf4j
public class Sequence implements Cloneable{

    private String name;
    private Model.Location location;
    private List<Media> medias;

    public  List<Media> getMedias() {
        if ( medias ==null )
            medias = new LinkedList();
        return medias;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
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
