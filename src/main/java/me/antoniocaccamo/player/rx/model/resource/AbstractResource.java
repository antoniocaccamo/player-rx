package me.antoniocaccamo.player.rx.model.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.time.Duration;

/**
 * @author antoniocaccamo on 18/02/2020
 */
@Getter @Setter
public abstract class AbstractResource {



    public enum TYPE {
        BLACK,
        HIDDEN,
        WATCH,
        WEATHER,
        PHOTO,
        VIDEO
    }

    public enum LOCATION {
        LOCAL,
        REMOTE
    }

    protected TYPE type;
    protected LOCATION location;
    protected String path;
    protected Duration duration;

    public abstract Path getLocalPath() ;
}
