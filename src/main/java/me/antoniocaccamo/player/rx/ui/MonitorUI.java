package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.CoatMux;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.event.media.command.CommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.PlayCommandEvent;
import me.antoniocaccamo.player.rx.event.media.progress.MediaEvent;
import me.antoniocaccamo.player.rx.event.media.progress.PercentageProgressMediaEvent;
import me.antoniocaccamo.player.rx.event.media.progress.StartedProgressMediaEvent;
import org.eclipse.swt.widgets.Composite;

import java.util.concurrent.TimeUnit;

/**
 * @author antoniocaccamo on 20/02/2020
 */
@Slf4j
public class MonitorUI extends CoatMux {

    private final PublishSubject<CommandEvent> commandEventSubject;
    private final PublishSubject<MediaEvent>   mediaEventSubject;

    public MonitorUI(Composite wrapped, PublishSubject<CommandEvent> commandEventSubject, PublishSubject<MediaEvent> mediaEventSubject) {
        super(wrapped);
        log.info("...");
        this.commandEventSubject = commandEventSubject;
        this.mediaEventSubject   = mediaEventSubject;
        this.commandEventSubject.subscribe(this::manageCommandEvent);
    }

    private void manageCommandEvent(CommandEvent evt) throws InterruptedException {
        log.info("event received : {}", evt);

        if ( evt instanceof PlayCommandEvent ) {
            PlayCommandEvent playCommandEvent = (PlayCommandEvent) evt;
            mediaEventSubject.onNext( new StartedProgressMediaEvent(playCommandEvent.getMedia()));

            Observable.intervalRange(1, 100, 100, 100, TimeUnit.MILLISECONDS)
                    .subscribe(along -> mediaEventSubject.onNext( new PercentageProgressMediaEvent(playCommandEvent.getMedia(), along.intValue())) );
        }
    }
}
