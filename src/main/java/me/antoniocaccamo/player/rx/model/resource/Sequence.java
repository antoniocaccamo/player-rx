package me.antoniocaccamo.player.rx.model.resource;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author antoniocaccamo on 18/02/2020
 */
@Getter @Setter
public class Sequence {

    public enum LOCATION {
        LOCAL,
        REMOTE
    }

    private String name;

    private LOCATION location;

    private List<AbstractResource> resources;

}
