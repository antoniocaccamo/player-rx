package me.antoniocaccamo.player.rx.event.media.command;

import me.antoniocaccamo.player.rx.event.media.MediaEvent;
import me.antoniocaccamo.player.rx.model.sequence.Media;

/**
 * @author antoniocaccamo on 20/02/2020
 */
public class PauseMediaEvent extends MediaEvent {
    protected PauseMediaEvent(Media media) {
        super(media);
    }
}
