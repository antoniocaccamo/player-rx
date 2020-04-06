package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.rx.RxBox;
import com.diffplug.common.rx.RxGetter;
import com.diffplug.common.swt.*;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Main;
import me.antoniocaccamo.player.rx.bundle.LocaleManager;
import me.antoniocaccamo.player.rx.event.media.command.*;
import me.antoniocaccamo.player.rx.event.media.progress.*;
import me.antoniocaccamo.player.rx.model.preference.MonitorModel;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.service.SequenceService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.*;

import javax.validation.constraints.NotNull;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author antoniocaccamo on 20/02/2020
 */
@Slf4j
public class TabItemMonitorUI extends CTabItem {

    @Getter
    private final int index;
    @Getter
    private final MonitorModel monitorModel;
    
    private final Shell monitorUI;

    private final SequenceService sequenceService;

    // tab -> monitor
    private final PublishSubject<CommandEvent> commandEventSubject = PublishSubject.create();

    // monitor -> tab
    private final PublishSubject<MediaEvent> mediaEventSubject = PublishSubject.create();
    private final ScheduledFuture<?> scheduledFuture;
    private final RxBox<StatusEnum> statusEnumRxBox;
    private ProgressBar progressBar;
    
    private Button playButton;
    private Button pauseButton;
    private Button stopButton;

    public TabItemMonitorUI(CTabFolder tabFolder, MonitorModel monitorModel,  int index) {
        super(tabFolder, SWT.NONE);

        sequenceService = Main.CONTEXT.findBean(SequenceService.class).get();

        setText(String.format("screen %s", index + 1));
        this.monitorModel = monitorModel;
        this.index = index;

        setControl(new Composite(getParent(), SWT.NONE) );
        Composite composite = (Composite) getControl();

        Layouts.setGrid(composite)
                .numColumns(1)
                .columnsEqualWidth(true)
                .margin(0)
        ;

        Layouts.setGridData(screenGroup(composite))
                .grabHorizontal();

        Layouts.setGridData(palimpsestGroup(composite))
                .grabAll();

        Layouts.setGridData(playerGroup(composite))
                .grabAll();

        monitorUI = Shells.builder(SWT.NONE, cmp -> {
            Layouts.setGrid(cmp).margin(0).spacing(0);
            Layouts.setGridData(new MonitorUI(cmp, index, commandEventSubject, mediaEventSubject)).grabAll();
        })
                .setSize(monitorModel.getSize().toPoint())
                .setLocation(monitorModel.getLocation().toPoint())
                .openOn(getParent().getShell())
        ;

        SwtExec.async().guardOn(this)
                .subscribe(
                        Single.create( emitter -> {
                            emitter.onSuccess(sequenceService.read(  Paths.get(monitorModel.getSequence())  ) );
                        }).toObservable()
                        , value -> log.debug("===>>>sequence : {}", value)
                );
                
        scheduledFuture =  SwtExec.async().guardOn(this)
                .scheduleAtFixedRate(
                        //()->log.debug("===>>> player #{}: check to run/stop...", this.index),
                        this::check,
                        100L, 
                        500L, 
                        TimeUnit.MILLISECONDS
                );

        this.mediaEventSubject.subscribe(
                mediaEvent -> log.debug("media event received : {}", mediaEvent)
        );

        // update progress bar
        mediaEventSubject.filter(me -> me instanceof PercentageProgressMediaEvent)
                .observeOn(  SwtExec.async().getRxExecutor().scheduler())
                .subscribe(
                        evt -> progressBar.setSelection( ((PercentageProgressMediaEvent)evt).getPercentage() ),
                        t -> log.error("error occurred on update % : {}", t)
                );

        mediaEventSubject.filter(me -> me instanceof  EndedProgressMediaEvent)
                .observeOn(  SwtExec.async().getRxExecutor().scheduler())
                .subscribe(
                        evt -> {
                            log.info("event received : {}", evt);
                            progressBar.setSelection( 0 );
                            commandEventSubject.onNext(
                                    new PlayCommandEvent(
                                            sequenceService.dummy().next())
                            );
                        },
                        t -> log.error("error occurred on update % : {}", t)
                );



        statusEnumRxBox = RxBox.of(status);
        statusEnumRxBox.asObservable()
                .observeOn(  SwtExec.async().getRxExecutor().scheduler())
                .subscribe(statusEnum ->  {
                    log.info("### status : {} ###", statusEnum);
                    switch (statusEnum){
                        case STOPPED:
                            progressBar.setSelection( 0 );
                        case NOT_ACTIVE:
                            stopButton.setEnabled(false);
                            pauseButton.setEnabled(false);
                            playButton.setEnabled(true);
                            break;
                        case PAUSED:
                        case PLAYING:
                            stopButton.setEnabled(true);
                            pauseButton.setEnabled(true);
                            playButton.setEnabled(false);
                            break;
                        default:
                    }
                });


        commandEventSubject
                .filter(commandEvent -> commandEvent instanceof StartCommandEvent)
                .map(commandEvent -> (StartCommandEvent) commandEvent)
                .observeOn(  SwtExec.async().getRxExecutor().scheduler())
                .subscribe(startCommandEvent -> {
                    log.info("startCommandEvent : {}", startCommandEvent);
                    statusEnumRxBox.set(   status = StatusEnum.PLAYING );
                    commandEventSubject.onNext(
                            new PlayCommandEvent(
                            sequenceService.dummy().next())
                    );
                });
    }

   

    @NotNull
    private Group screenGroup(Composite composite) {
        Label label     = null;
        Spinner spinner = null;

        final Group group = new Group(composite, SWT.SHADOW_ETCHED_IN);
        group.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Screen));

        Layouts.setGrid(group)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;

        // -- size
        final Group sizeGroup = new Group(group, SWT.NONE);
        sizeGroup.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Size.Size));
        Layouts.setGrid(sizeGroup)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;
        Layouts.setGridData(sizeGroup).grabHorizontal();
        label = new Label(sizeGroup, SWT.NONE);
        label.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Size.Width));
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(sizeGroup, SWT.NONE);
        spinner.setMinimum(0);spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(monitorModel.getSize().getWidth());
        SwtRx.addListener(spinner, SWT.Modify , SWT.Selection)
                .subscribe(
                        evt -> {
                            monitorModel.getSize().setWidth(((Spinner) evt.widget).getSelection());
                            log.info("monitorModel.getSize().getWidth()  : {}", monitorModel.getSize().getWidth());
                            monitorUI.setSize(monitorModel.getSize().toPoint());
                        });

        label = new Label(sizeGroup, SWT.NONE);
        label.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Size.Height));
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(sizeGroup, SWT.NONE);
        spinner.setMinimum(0); spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(monitorModel.getSize().getHeight());
        SwtRx.addListener(spinner, SWT.Modify , SWT.Selection)
                .subscribe(
                        evt -> {
                            monitorModel.getSize().setHeight(((Spinner) evt.widget).getSelection());
                            log.info("monitorModel.getSize().getHeight() : {}", monitorModel.getSize().getHeight());
                            monitorUI.setSize(monitorModel.getSize().toPoint());
                        });

        // -- location
        final Group locationGroup = new Group(group, SWT.NONE);
        locationGroup.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Location.Location));
        Layouts.setGrid(locationGroup)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;
        Layouts.setGridData(locationGroup).grabHorizontal();
        label = new Label(locationGroup, SWT.NONE);
        label.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Location.Top));
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(locationGroup, SWT.NONE);
        spinner.setMinimum(0);spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(monitorModel.getLocation().getTop());
        SwtRx.addListener(spinner, SWT.Modify , SWT.Selection)
                .subscribe(
                        evt -> {
                            monitorModel.getLocation().setTop(((Spinner) evt.widget).getSelection());
                            log.info("monitorModel.getLocation().getTop()  : {}", monitorModel.getLocation().getTop());
                            monitorUI.setLocation(monitorModel.getLocation().toPoint());
                        });

        label = new Label(locationGroup, SWT.NONE);
        label.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Location.Left));
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(locationGroup, SWT.NONE);
        spinner.setMinimum(0); spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(monitorModel.getLocation().getLeft());
        SwtRx.addListener(spinner, SWT.Modify , SWT.Selection)
                .subscribe(
                        evt -> {
                            monitorModel.getLocation().setLeft(((Spinner) evt.widget).getSelection());
                            log.info("monitorModel.getLocation().getLeft() : {}", monitorModel.getLocation().getLeft());
                            monitorUI.setLocation(monitorModel.getLocation().toPoint());
                        });

        // watch
        final Group watchGroup = new Group(group, SWT.NONE);
        watchGroup.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Watch.Watch));
        Layouts.setGrid(watchGroup)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;
        Layouts.setGridData(watchGroup).horizontalSpan(2).grabHorizontal();
        final Group watchBackgroundGroup = new Group(watchGroup, SWT.NONE);
        watchBackgroundGroup.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Watch.Background));
        Layouts.setGrid(watchBackgroundGroup)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0);

        Button watchBackgroundImageButton = new Button(watchBackgroundGroup, SWT.CHECK);
        watchBackgroundImageButton.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Watch.BackgroundImage));
        Layouts.setGridData(watchBackgroundGroup).grabHorizontal();

        Button watchBackgroundImageFileButton = new Button(watchBackgroundGroup, SWT.PUSH);
        watchBackgroundImageFileButton.setText(LocaleManager.getText(LocaleManager.Application.Search.File));
        Layouts.setGridData(watchBackgroundImageFileButton).grabHorizontal();

        final Group watchFontGroup = new Group(watchGroup, SWT.NONE);
        watchFontGroup.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Watch.Font));
        Layouts.setGrid(watchFontGroup)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0);
        Layouts.setGridData(watchFontGroup).grabHorizontal();

        Button watchFontDataButton = new Button(watchFontGroup, SWT.PUSH);
        watchFontDataButton.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Watch.FontDate));
        Layouts.setGridData(watchFontDataButton).grabHorizontal();

        Button watchFontTimeButton = new Button(watchFontGroup, SWT.PUSH);
        watchFontTimeButton.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Watch.FontTime));
        Layouts.setGridData(watchFontTimeButton).grabHorizontal();

        return group;
    }

    private Group palimpsestGroup(Composite composite) {
        Label label = null;
        Spinner spinner = null;

        final Group group = new Group(composite, SWT.NONE);
        group.setText(LocaleManager.getText(LocaleManager.Application.Group.Activation.Activation));

        Layouts.setGrid(group)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;

        return group;
    }

    @NotNull
    private Group playerGroup(Composite composite) {

        Label label = null;
        Button button =null;

        Group group = new Group(composite, SWT.NONE);
        group.setText(LocaleManager.getText(LocaleManager.Application.Group.Sequence.Sequence));

        Layouts.setGrid(group)
                .numColumns(2)
                .columnsEqualWidth(false)
                .margin(0)
        ;

        label = new Label(group, SWT.NONE);
        label.setText(LocaleManager.getText(LocaleManager.Application.Group.Sequence.Sequence));

        Layouts.setGridData(new Combo(group, SWT.NONE)).grabHorizontal();

        label = new Label(group, SWT.NONE);
        label.setText(LocaleManager.getText("Numero video"));

        label = new Label(group, SWT.NONE);
        label.setText(LocaleManager.getText("3"));

        label = new Label(group, SWT.NONE);
        label.setText(LocaleManager.getText("Durata"));

        label = new Label(group, SWT.NONE);
        label.setText("00:00:09,000");

        label = new Label(group, SWT.NONE);
        label.setText(LocaleManager.getText("Nome file"));

        label = new Label(group, SWT.NONE);
        label.setText("default.xsq");

        Composite buttonsComposite = new Composite(group, SWT.NONE);
        Layouts.setGrid(buttonsComposite).numColumns(3).columnsEqualWidth(true);
        Layouts.setGridData(buttonsComposite)
                .horizontalSpan(2)
                .grabHorizontal()
        ;

        stopButton = new Button(buttonsComposite, SWT.PUSH);
        stopButton.setText("Stop");
        Layouts.setGridData(stopButton)
                .grabHorizontal()
                .minimumWidth(SwtMisc.defaultButtonWidth())
        ;
        SwtRx.addListener(stopButton, SWT.Selection).subscribe(evt -> {
            log.info("stop  pressed ..");
            commandEventSubject.onNext( new StopCommandEvent());
            statusEnumRxBox.set(StatusEnum.STOPPED);
        });

        pauseButton = new Button(buttonsComposite, SWT.PUSH);
        pauseButton.setText("Pause");
        Layouts.setGridData(pauseButton)
                .grabHorizontal()
                .minimumWidth(SwtMisc.defaultButtonWidth())
        ;
        SwtRx.addListener(pauseButton, SWT.Selection).subscribe(evt ->{
            log.info("pause pressed ..");
            commandEventSubject.onNext( new PauseCommandEvent(null));
            statusEnumRxBox.set(StatusEnum.PAUSED);
        });

        playButton = new Button(buttonsComposite, SWT.PUSH);
        playButton.setText("Play");
        Layouts.setGridData(playButton)
                .grabHorizontal()
                .minimumWidth(SwtMisc.defaultButtonWidth())
        ;
        SwtRx.addListener(playButton, SWT.Selection).subscribe(evt ->{
            log.info("play pressed ..");
//            commandEventSubject.onNext( new PlayCommandEvent(
//                    Media.builder()
//                            .resource(
//                                    LocalResource.builder()
//                                            .withDuration(Duration.ofSeconds(5))
//                                            .withType(Resource.TYPE.BLACK)
//                                            .build()
//                            )
//                    .build()
//
//            )) ;
            statusEnumRxBox.set(StatusEnum.PLAYING);
        });


        Group mediaGroup  = new Group(group, SWT.NONE);
        mediaGroup.setText("current media");
        Layouts.setGrid(mediaGroup).numColumns(2).columnsEqualWidth(false)
            //.margin(2)
            .spacing(2);
        Layouts.setGridData(mediaGroup)
                .horizontalSpan(2)
                .grabHorizontal()
        ;

        label = new Label(mediaGroup, SWT.NONE);
        label.setText(LocaleManager.getText("Numero video"));

        label = new Label(mediaGroup, SWT.NONE);
        label.setText(LocaleManager.getText("3"));

        label = new Label(mediaGroup, SWT.NONE);
        label.setText(LocaleManager.getText("Durata"));

        label = new Label(mediaGroup, SWT.NONE);
        label.setText("00:00:09,000");

        label = new Label(mediaGroup, SWT.NONE);
        label.setText(LocaleManager.getText("Nome file"));

        label = new Label(mediaGroup, SWT.NONE);
        label.setText("default.xsq");

        progressBar = new ProgressBar(mediaGroup, SWT.NONE);

        Layouts.setGridData( progressBar )
                .horizontalSpan(2)
                .grabHorizontal()
        ;
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);



        return group;
    }

    private void check() {
        log.debug("getIndex() [{}] running [{}] status [{}] getPlayerSetting().isTimed() [{}] ", getIndex(), getRunning(), getStatus() , getMonitorModel().isTimed());

        if ( StatusEnum.STOPPED.equals( getStatus()) ) {
            return;
        }

        if ( ! monitorModel.isTimed() ) {

            if ( ! RunningEnum.N.equals(getRunning()) || ! ( StatusEnum.PLAYING.equals(getStatus()) || StatusEnum.PAUSED.equals(getStatus())) ) {

               commandEventSubject.onNext( new StartCommandEvent() );
            }

        }

        if ( monitorModel.isTimed() ) {

            LocalTime now   = LocalTime.now();

            if ( RunningEnum.Y.equals(getRunning()) ) {

                log.debug("*** now [{}] getIndex() [{}] getStatus() [{}] getPlayerSetting().getActiveFrom() [{}]  getPlayerSetting().getActiveTo() [{}]",
                        now,
                        getIndex(),
                        getStatus() ,
                        getMonitorModel().getFrom(),
                        getMonitorModel().getTo()
                );

                switch ( getStatus() ) {

                    case PAUSED:
                    case PLAYING:

                        if ( (now.isBefore( getMonitorModel().getFrom() ) || now.isAfter( getMonitorModel().getTo() ))  ) {
                            log.warn("*** it's time to pause :  now [{}] getIndex() [{}] getStart() [{}] getEnd() [{}]",
                                    now,
                                    getIndex(),
                                    getMonitorModel().getFrom(),
                                    getMonitorModel().getTo()
                            );
                            commandEventSubject.onNext( new DeactivateCommandEvent() );
                        }

                        break;

                    case NOT_ACTIVE:

                        if ( getMonitorModel().getFrom().isBefore(now) && now.isBefore(getMonitorModel().getTo()) ) {
                            log.warn("*** it's time to play :  now [{}] getIndex() [{}] getStart() [{}] getEnd() [{}]",
                                    now,
                                    getIndex(),
                                    getMonitorModel().getFrom(),
                                    getMonitorModel().getTo()
                            );
//                            dayOfFirstRun = now.toLocalDate();
                            commandEventSubject.onNext( new StartCommandEvent() );
                        }

                        break;

                    default:
                        break;
                }

            }
        }
    }

    @Override
    public void dispose() {
        log.warn("disposing tabItemMonitorUI : #{}", index);
        this.commandEventSubject.onComplete();
        this.mediaEventSubject.onComplete();
        scheduledFuture.cancel(true);
        monitorUI.dispose();
        super.dispose();
    }
    
    @Getter @Setter
    private StatusEnum status = StatusEnum.NOT_ACTIVE;

    public enum StatusEnum {
        NOT_ACTIVE, 
        STOPPED, 
        PLAYING, 
        PAUSED 
    }
    
    public enum RunningEnum {
        N, Y
    }
    
    @Getter @Setter
    private RunningEnum running = RunningEnum.N;
    
    
}
