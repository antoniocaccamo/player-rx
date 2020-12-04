package me.antoniocaccamo.player.rx.event.monitor;

import lombok.Getter;
import me.antoniocaccamo.player.rx.event.media.MediaEvent;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author antoniocaccamo  on 16/11/2020
 */

@Getter
public class MonitorMediaEvent {
    
    private final int monitorId;
    private final MediaEvent mediaEvent;

    public MonitorMediaEvent(int monitorId, MediaEvent mediaEvent) {
        this.monitorId = monitorId;
        this.mediaEvent = mediaEvent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("monitorId", monitorId)
                .append("mediaEvent", mediaEvent)
                .toString();
    }


    public static MonitorMediaEventBuilder builder() {
        return new MonitorMediaEventBuilder();
    }


    public static final class MonitorMediaEventBuilder {
        private int monitorId;
        private MediaEvent mediaEvent;

        private MonitorMediaEventBuilder() {
        }



        public MonitorMediaEventBuilder monitorId(int monitorId) {
            this.monitorId = monitorId;
            return this;
        }

        public MonitorMediaEventBuilder mediaEvent(MediaEvent mediaEvent) {
            this.mediaEvent = mediaEvent;
            return this;
        }

        public MonitorMediaEvent build() {
            return new MonitorMediaEvent(monitorId, mediaEvent);
        }
    }
}
