package me.antoniocaccamo.player.rx.event.media.progress;

import lombok.Getter;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author antoniocaccamo on 20/02/2020
 */

public class PercentageProgressMediaEvent extends MediaEvent {

    @Getter
    final  int percentage ;

    public PercentageProgressMediaEvent(Media media, int percentage) {
        super(media);
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("percentage", percentage)
                .toString();
    }
}
