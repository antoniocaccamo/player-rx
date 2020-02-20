package me.antoniocaccamo.player.rx.event.media.progress;

import me.antoniocaccamo.player.rx.event.media.MediaEvent;
import me.antoniocaccamo.player.rx.model.sequence.Media;

/**
 * @author antoniocaccamo on 20/02/2020
 */
public class PercentageProgressMediaEvent extends MediaEvent {
    protected PercentageProgressMediaEvent(Media media) {
        super(media);
    }
}
