package me.antoniocaccamo.player.rx.model.resource;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;

/**
 * @author antoniocaccamo on 18/02/2020
 */
@Getter @Setter @Builder
public class FTPRemoreResource extends RemoteResource {



    @Override
    public Path getLocalPath() {
        return null;
    }
}
