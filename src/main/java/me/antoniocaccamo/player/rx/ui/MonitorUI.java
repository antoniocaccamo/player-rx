package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.CoatMux;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.SwtExec;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.event.media.command.CommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.PlayCommandEvent;
import me.antoniocaccamo.player.rx.event.media.progress.*;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.ui.monitor.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author antoniocaccamo on 20/02/2020
 */
@Slf4j
public class MonitorUI extends CoatMux {

    private final PublishSubject<CommandEvent> commandEventSubject;
    private final PublishSubject<MediaEvent>   mediaEventSubject;

    private final Map<Resource.TYPE, CoatMux.Layer<AbstractUI> > layerMap = new HashMap<>();
    private final int index;

    private  CoatMux.Layer<AbstractUI> currenLayer;

    public MonitorUI(Composite wrapped, int index, PublishSubject<CommandEvent> commandEventSubject, PublishSubject<MediaEvent> mediaEventSubject) {
        super(wrapped, SWT.NONE);
        this.index = index;
        log.info("monitor # {}", getIndex() );
        this.commandEventSubject = commandEventSubject;
        this.mediaEventSubject   = mediaEventSubject;
        this.commandEventSubject
                .observeOn(  SwtExec.async().getRxExecutor().scheduler())
                .subscribe(this::manageCommandEvent);
        createSubMonitor();

    }

    public int getIndex() {
        return index;
    }

    private void createSubMonitor() {
        // Resource.TYPE.BLACK
        layerMap.putIfAbsent(Resource.TYPE.BLACK, addCoat( composite -> {
            Layouts.setGrid(composite)
                    .numColumns(1)
                    .columnsEqualWidth(true)
                    .horizontalSpacing(0)
                    .verticalSpacing(0)
                    .spacing(0)
                    .margin(0)
            ;
            return new BlackUI(this, composite);

        }));

        // Resource.TYPE.WATCH
        layerMap.putIfAbsent(Resource.TYPE.WATCH, addCoat( composite -> {
            Layouts.setGrid(composite)
                    .numColumns(1)
                    .columnsEqualWidth(true)
                    .horizontalSpacing(0)
                    .verticalSpacing(0)
                    .spacing(0)
                    .margin(0)
            ;
            return new WatchUI(this, composite);

        }));

        // Resource.TYPE.WEATHER
        layerMap.putIfAbsent(Resource.TYPE.WEATHER, addCoat( composite -> {
            Layouts.setGrid(composite)
                    .numColumns(1)
                    .columnsEqualWidth(true)
                    .horizontalSpacing(0)
                    .verticalSpacing(0)
                    .spacing(0)
                    .margin(0)
            ;
            return new WeatherUI(this, composite);
        }));

        // Resource.TYPE.PHOTO
        layerMap.putIfAbsent(Resource.TYPE.PHOTO, addCoat( composite -> {
            Layouts.setGrid(composite)
                    .numColumns(1)
                    .columnsEqualWidth(true)
                    .horizontalSpacing(0)
                    .verticalSpacing(0)
                    .spacing(0)
                    .margin(0)
            ;
            return new PhotoUI(this, composite);
        }));

        // Resource.TYPE.VIDEO
        layerMap.putIfAbsent(Resource.TYPE.VIDEO, addCoat( composite -> {
            Layouts.setGrid(composite)
                    .numColumns(1)
                    .columnsEqualWidth(true)
                    .horizontalSpacing(0)
                    .verticalSpacing(0)
                    .spacing(0)
                    .margin(0)
            ;
            return new VideoUI(this, composite);
        }));

    }

    private void manageCommandEvent(CommandEvent evt) throws InterruptedException {
        log.info("event received : {}", evt);

        if ( evt instanceof PlayCommandEvent ) {
            PlayCommandEvent playCommandEvent = (PlayCommandEvent) evt;
            play(  playCommandEvent.getMedia() );
        }
    }

    public void play( Media media ) {
        log.info("playing media : {}", media);
        currenLayer  = this.layerMap.get(  media.getResource().getType() );
        currenLayer.getHandle().setMedia(media);
        currenLayer.getHandle().play();
        currenLayer.bringToTop();
        log.info("showing : {}", currenLayer.getHandle().getClass().getSimpleName());
        mediaEventSubject.onNext( new StartedProgressMediaEvent(media));
    }

    public void errorOnPlay(Throwable throwable){
        this.mediaEventSubject.onNext(
                new ErrorProgressMediaEvent(currenLayer.getHandle().getMedia(), throwable)
        );
    }

    public void next() {
        this.mediaEventSubject.onNext(
                new EndedProgressMediaEvent(currenLayer.getHandle().getMedia())
        );
    }

    public void stop() {
        currenLayer.getHandle().stop();
    }

    public void updatePercentageProgess(int percentage){
        mediaEventSubject
                .onNext( new PercentageProgressMediaEvent(currenLayer.getHandle().getMedia(), percentage));
    }

    @Override
    public void dispose() {
        stop();
        log.info("dispose on monitor : {}", getIndex());
    }
}
