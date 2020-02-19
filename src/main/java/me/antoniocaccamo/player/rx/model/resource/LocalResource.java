package me.antoniocaccamo.player.rx.model.resource;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * @author antoniocaccamo on 18/02/2020
 */
@Getter
@Setter
public class LocalResource extends AbstractResource {

    @Override
    public Path getLocalPath() {
        return null;
    }
}
