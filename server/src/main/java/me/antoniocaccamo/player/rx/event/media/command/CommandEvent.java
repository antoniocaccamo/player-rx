package me.antoniocaccamo.player.rx.event.media.command;

import lombok.Getter;
import me.antoniocaccamo.player.rx.event.media.MediaEvent;

/**
 * @author antoniocaccamo on 20/02/2020
 */
public abstract class CommandEvent extends MediaEvent {

    @Getter
    protected final CommandEventType type;

    protected CommandEvent(CommandEventType type) {
        this.type = type;
    }

    public enum CommandEventType {
        DEATIVATE,
        PLAY,
        PAUSE,
        RESUME,
        START,
        STOP
    }


}
