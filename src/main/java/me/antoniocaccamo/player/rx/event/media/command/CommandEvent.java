package me.antoniocaccamo.player.rx.event.media.command;

import lombok.Getter;
import me.antoniocaccamo.player.rx.model.sequence.Media;

/**
 * @author antoniocaccamo on 20/02/2020
 */
public abstract class CommandEvent {

    @Getter
    private final Media media;

    public CommandEvent(Media media) {
        this.media = media;
    }
}
