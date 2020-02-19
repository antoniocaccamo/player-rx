package me.antoniocaccamo.player.rx.model.sequence;

import lombok.Getter;
import lombok.Setter;
import me.antoniocaccamo.player.rx.model.Model;

import java.util.List;

@Getter @Setter
public class Sequence implements Cloneable{

    private String name;
    private Model.Location location;
    private List<Media> medias;
}
