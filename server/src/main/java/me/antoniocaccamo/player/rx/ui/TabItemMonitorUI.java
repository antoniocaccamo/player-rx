package me.antoniocaccamo.player.rx.ui;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Optional;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.validation.constraints.NotNull;

import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtExec;
import com.diffplug.common.swt.SwtMisc;
import com.diffplug.common.swt.SwtRx;

import me.antoniocaccamo.player.rx.event.monitor.MonitorMediaEvent;
import me.antoniocaccamo.player.rx.model.preference.LoadedSequence;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Application;
import me.antoniocaccamo.player.rx.ApplicationUI;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.event.media.command.CommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.DeactivateCommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.PauseCommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.PlayCommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.ResumeCommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.StartCommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.StopCommandEvent;
import me.antoniocaccamo.player.rx.event.media.progress.EndedProgressMediaEvent;
import me.antoniocaccamo.player.rx.event.media.progress.ProgressEvent;
import me.antoniocaccamo.player.rx.event.media.progress.PercentageProgressMediaEvent;
import me.antoniocaccamo.player.rx.helper.LocaleHelper;
import me.antoniocaccamo.player.rx.model.preference.Screen;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.model.sequence.SequenceLooper;
import me.antoniocaccamo.player.rx.service.SequenceService;

/**
 * @author antoniocaccamo on 20/02/2020
 */
@Slf4j
// @Configurable(preConstruction = true)
public class TabItemMonitorUI extends CTabItem {

    @Getter
    private final int index;

    @Getter
    private final Screen screen;

    private final Shell monitorUI;

    // @Autowired
    private final SequenceService sequenceService;

    // tab -> monitor
//    private final PublishSubject<CommandEvent> commandEventSubject = PublishSubject.create();

    // monitor -> tab
//    private final PublishSubject<ProgressEvent> mediaEventSubject = PublishSubject.create();

    private final SequenceLooper sequenceLooper = new SequenceLooper();

    @Getter
    private ScreenUI screenUI;

    // private Optional<Sequence> selectedSequence;

    private ScheduledFuture<?> scheduledFuture;
    private RxBox<StatusEnum> statusEnumRxBox;
    private ProgressBar progressBar;

    // -- swt

    private Button playButton;
    private Button pauseButton;
    private Button stopButton;
    private Combo sequenceCombo;

    private Label labelNomeFile;

    private Disposable endedProgressMediaEventDisposable;

    public TabItemMonitorUI(CTabFolder tabFolder, Screen screen, int index) {
        super(tabFolder, SWT.NONE);

        sequenceService = Application.CONTEXT.getBean(SequenceService.class);
        // log.info( "sequenceService == null : {}", sequenceService == null );

        setText(String.format("screen %s", index));
        this.screen = screen;
        this.index = index;

        setControl(new Composite(getParent(), SWT.NONE));
        Composite composite = (Composite) getControl();

        Layouts.setGrid(composite).numColumns(1).columnsEqualWidth(true).margin(0);
        // screen
        Layouts.setGridData(groupScreen(composite)).grabHorizontal();

        // palimpsest
        Layouts.setGridData(groupPalimpsest(composite)).grabAll();

        // player
        Layouts.setGridData(groupPlayer(composite)).grabAll();

        // monitor
        monitorUI = Shells.builder(SWT.NONE, cmp -> {
            Layouts.setGrid(cmp).margin(0).spacing(0);
            this.screenUI = new ScreenUI(cmp, index);
            Layouts.setGridData(screenUI).grabAll();
        }).setSize(screen.getSize().toPoint()).setLocation(screen.getLocation().toPoint())
                // .openOn(getParent().getShell())
                .setImage(ApplicationUI.ImageUI).openOnActive();
        sequenceLooper.setLoadedSequence(sequenceService.getLoadedSequenceByName(screen.getSequence())
                .orElse(LoadedSequence.builder().name("NO LOADEDED SEQUENCE").build()));

        // create observers
        // SwtExec.async().guardOn(this)
        // .subscribe(
        // Single.create(emitter -> {
        // emitter.onSuccess(sequenceService.getSequenceByName( screen.getSequence()) );
        // }).toObservable()
        // , value -> {
        // Optional<Sequence> sequence = (Optional<Sequence>) value;
        // // selectedSequence = sequence;
        // sequenceLooper.setOptionalSequence(sequence);
        // sequence.ifPresent(sq -> log.info("getIndex() [{}] - sequence : {}" ,
        // getIndex(), sq));
        // }
        // );

    }

    public boolean applyScreen() {

        log.info("getIndex() [{}] - apply screen : {}", getIndex(), getScreen());

        Observable.fromIterable(sequenceService.getLoadedSequences())
                .subscribe(lsq -> sequenceCombo.add(lsq.getName()));

        createObservers();
        createTimers();
        return true;
    }

    private void createObservers() {

        log.info("getIndex() [{}] - createObservers", getIndex());

        //mediaEventSubject
        ApplicationUI.MONITOR_MEDIA_EVENT_BUS
            .filter(mme -> mme.getMonitorId() == getIndex())
            .map(mme -> mme.getMediaEvent())
            .subscribe( me -> log.debug("getIndex() [{}] - media event received : {}", getIndex(), me));

        // update progress bar
        //mediaEventSubject
        ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                .filter(mme -> mme.getMonitorId() == getIndex() && mme.getMediaEvent()  instanceof PercentageProgressMediaEvent)
            .map(mme -> (PercentageProgressMediaEvent) mme.getMediaEvent())
            .observeOn(SwtExec.async().getRxExecutor().scheduler())
            .subscribe(
                evt -> {
                    progressBar.setMaximum((int) evt.getTotal());
                    progressBar.setSelection((int) evt.getActual());
                    progressBar.setToolTipText(String.format("%d : %d", (int) evt.getActual(), (int) evt.getTotal()));
                    if (log.isDebugEnabled())
                        log.debug("getIndex() [{}] -  PercentageProgressMediaEvent : : {}", getIndex(), evt);
                },
                t -> log.error("getIndex() [{}] - error occurred on update : {}", getIndex(), t)
            );

        endedProgressMediaEventDisposable = // mediaEventSubject
        ApplicationUI.MONITOR_MEDIA_EVENT_BUS
            .filter(mme -> mme.getMonitorId() == getIndex() && mme.getMediaEvent() instanceof EndedProgressMediaEvent)
            .map(mme -> (EndedProgressMediaEvent) mme.getMediaEvent())
            .observeOn(SwtExec.async().getRxExecutor().scheduler())
            .subscribe(
                evt -> {
                    log.debug("getIndex() [{}] - event received : {}", getIndex(), evt);
                    progressBar.setSelection(0);
                    Optional<Media> optionalMedia = sequenceLooper.next();
                    Media media = optionalMedia.orElse(getWhenNotActiveMedia());
                    ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                        .onNext(MonitorMediaEvent.builder()
                            .monitorId(getIndex())
                            .mediaEvent(new PlayCommandEvent(media))
                            .build()
                    );
                    //commandEventSubject.onNext(new PlayCommandEvent(media));
                    labelNomeFile.setText(media.toString());
                },
                t -> log.error("getIndex() [{}] - error occurred on update % : {}", getIndex(), t)
            );

        statusEnumRxBox = RxBox.of(status);
        statusEnumRxBox.asObservable()
            .observeOn(SwtExec.async().getRxExecutor().scheduler())
            .subscribe(statusEnum -> {
                log.info("### getIndex() [{}] - status : {} ###", getIndex(), statusEnum);
                status = statusEnum;
                switch (statusEnum) {
                    case STOPPED:
                        progressBar.setSelection(0);
                        running = RunningEnum.N;
                        pauseButton.setSelection(false);
                    case NOT_ACTIVE:
                        stopButton.setEnabled(false);
                        pauseButton.setEnabled(false);
                        playButton.setEnabled(true);
                        sequenceCombo.setEnabled(true);
                        break;
                    case PAUSED:
                    case PLAYING:
                        stopButton.setEnabled(true);
                        pauseButton.setEnabled(true);
                        playButton.setEnabled(false);
                        sequenceCombo.setEnabled(false);
                        break;
                    default:
                }
            });

        sequenceCombo.addModifyListener(e -> {
            if (StringUtils.isEmpty(sequenceCombo.getText()))
                return;
            sequenceLooper.setLoadedSequence(sequenceService.getLoadedSequenceByName(screen.getSequence())
                    .orElse(LoadedSequence.builder().name("NO LOADEDED SEQUENCE").build()));
            screen.setSequence(sequenceCombo.getText());
        });

        //commandEventSubject
        ApplicationUI.MONITOR_MEDIA_EVENT_BUS
            .filter(mme -> mme.getMonitorId() == getIndex() && mme.getMediaEvent() instanceof StartCommandEvent)
            .map(mme -> (StartCommandEvent) mme.getMediaEvent())
            .observeOn(SwtExec.async().getRxExecutor().scheduler())
            .subscribe(startCommandEvent -> {
                log.info("getIndex() [{}] - startCommandEvent  {}", getIndex(), startCommandEvent);
                // @TODO : remove getOptionalSequence
                if (sequenceLooper.getLoadedSequence().hasSequence()) {
                    log.info("getIndex() [{}] - start playing sequence : {} ", getIndex(),
                            sequenceLooper.getLoadedSequence().getName());
                    running = RunningEnum.Y;
                    sequenceCombo.setText(sequenceLooper.getLoadedSequence().getName());
                    statusEnumRxBox.set(status = StatusEnum.PLAYING);
                    Optional<Media> optionalMedia = sequenceLooper.next();
                    Media media = optionalMedia.map(m -> m).orElse(getWhenNotActiveMedia());
                    labelNomeFile.setText(media.toString());
                    ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                        .onNext(MonitorMediaEvent.builder()
                                .monitorId(getIndex())
                                .mediaEvent(new PlayCommandEvent(media))
                                .build()
                        );
//                  commandEventSubject.onNext(new PlayCommandEvent(media));
                }
            });
    }

    private void createTimers() {

        log.info("getIndex() [{}] - createTimers", getIndex());

        scheduledFuture = SwtExec.async().guardOn(this).scheduleAtFixedRate(
                // ()->log.debug("===>>> player #{}: check to run/stop...", this.index),
                this::check, 100L, 500L, TimeUnit.MILLISECONDS);
    }

    private Media getWhenNotActiveMedia() {
        return Media.builder().duration(Duration.ofSeconds(5))
                .resource(LocalResource.builder().withType(Constants.Resource.Type.WATCH).build()).build();
    }

    @NotNull
    private Group groupScreen(Composite composite) {
        Label label = null;
        Spinner spinner = null;

        final Group group = new Group(composite, SWT.SHADOW_ETCHED_IN);
        group.setText(LocaleHelper.Application.Group.Screen.Screen);

        Layouts.setGrid(group).numColumns(2).columnsEqualWidth(true).margin(0);

        // -- size
        final Group sizeGroup = new Group(group, SWT.NONE);
        sizeGroup.setText(LocaleHelper.Application.Group.Screen.Size.Size);
        Layouts.setGrid(sizeGroup).numColumns(2).columnsEqualWidth(true).margin(0);
        Layouts.setGridData(sizeGroup).grabHorizontal();
        label = new Label(sizeGroup, SWT.NONE);
        label.setText(LocaleHelper.Application.Group.Screen.Size.Width);
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(sizeGroup, SWT.NONE);
        spinner.setMinimum(0);
        spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(screen.getSize().getWidth());
        SwtRx.addListener(spinner, SWT.Modify, SWT.Selection).subscribe(evt -> {
            screen.getSize().setWidth(((Spinner) evt.widget).getSelection());
            log.info("getIndex() [{}] - monitorModel.getSize().getWidth()  : {}", getIndex(),
                    screen.getSize().getWidth());
            monitorUI.setSize(screen.getSize().toPoint());
        });

        label = new Label(sizeGroup, SWT.NONE);
        label.setText(LocaleHelper.Application.Group.Screen.Size.Height);
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(sizeGroup, SWT.NONE);
        spinner.setMinimum(0);
        spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(screen.getSize().getHeight());
        SwtRx.addListener(spinner, SWT.Modify, SWT.Selection).subscribe(evt -> {
            screen.getSize().setHeight(((Spinner) evt.widget).getSelection());
            log.info("monitorModel.getSize().getHeight() : {}", screen.getSize().getHeight());
            monitorUI.setSize(screen.getSize().toPoint());
        });

        // -- location
        final Group locationGroup = new Group(group, SWT.NONE);
        locationGroup.setText(LocaleHelper.Application.Group.Screen.Location.Location);
        Layouts.setGrid(locationGroup).numColumns(2).columnsEqualWidth(true).margin(0);
        Layouts.setGridData(locationGroup).grabHorizontal();
        label = new Label(locationGroup, SWT.NONE);
        label.setText(LocaleHelper.Application.Group.Screen.Location.Top);
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(locationGroup, SWT.NONE);
        spinner.setMinimum(0);
        spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(screen.getLocation().getTop());
        SwtRx.addListener(spinner, SWT.Modify, SWT.Selection).subscribe(evt -> {
            screen.getLocation().setTop(((Spinner) evt.widget).getSelection());
            log.info("getIndex() [{}] - monitorModel.getLocation().getTop()  : {}", getIndex(),
                    screen.getLocation().getTop());
            monitorUI.setLocation(screen.getLocation().toPoint());
        });

        label = new Label(locationGroup, SWT.NONE);
        label.setText(LocaleHelper.Application.Group.Screen.Location.Left);
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(locationGroup, SWT.NONE);
        spinner.setMinimum(0);
        spinner.setMaximum(Integer.MAX_VALUE);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(screen.getLocation().getLeft());
        SwtRx.addListener(spinner, SWT.Modify, SWT.Selection).subscribe(evt -> {
            screen.getLocation().setLeft(((Spinner) evt.widget).getSelection());
            log.info("getIndex() [{}] - monitorModel.getLocation().getLeft() : {}", getIndex(),
                    screen.getLocation().getLeft());
            monitorUI.setLocation(screen.getLocation().toPoint());
        });

        // watch
        final Group watchGroup = new Group(group, SWT.NONE);
        watchGroup.setText(LocaleHelper.Application.Group.Screen.Watch.Watch);
        Layouts.setGrid(watchGroup).numColumns(2).columnsEqualWidth(true).margin(0);
        Layouts.setGridData(watchGroup).horizontalSpan(2).grabHorizontal();
        final Group watchBackgroundGroup = new Group(watchGroup, SWT.NONE);
        watchBackgroundGroup.setText(LocaleHelper.Application.Group.Screen.Watch.Background);
        Layouts.setGrid(watchBackgroundGroup).numColumns(2).columnsEqualWidth(true).margin(0);

        Button watchBackgroundImageButton = new Button(watchBackgroundGroup, SWT.CHECK);
        watchBackgroundImageButton.setText(LocaleHelper.Application.Group.Screen.Watch.BackgroundImage);
        Layouts.setGridData(watchBackgroundGroup).grabHorizontal();

        Button watchBackgroundImageFileButton = new Button(watchBackgroundGroup, SWT.PUSH);
        watchBackgroundImageFileButton.setText(LocaleHelper.Application.Search.File);
        Layouts.setGridData(watchBackgroundImageFileButton).grabHorizontal();

        final Group watchFontGroup = new Group(watchGroup, SWT.NONE);
        watchFontGroup.setText(LocaleHelper.Application.Group.Screen.Watch.Font.Font);
        Layouts.setGrid(watchFontGroup).numColumns(2).columnsEqualWidth(true).margin(0);
        Layouts.setGridData(watchFontGroup).grabHorizontal();

        Button watchFontDataButton = new Button(watchFontGroup, SWT.PUSH);
        watchFontDataButton.setText(LocaleHelper.Application.Group.Screen.Watch.Font.FontDate);
        Layouts.setGridData(watchFontDataButton).grabHorizontal();

        Button watchFontTimeButton = new Button(watchFontGroup, SWT.PUSH);
        watchFontTimeButton.setText(LocaleHelper.Application.Group.Screen.Watch.Font.FontTime);
        Layouts.setGridData(watchFontTimeButton).grabHorizontal();

        return group;
    }

    private Group groupPalimpsest(Composite composite) {
        Label label = null;
        Spinner spinner = null;

        final Group group = new Group(composite, SWT.NONE);
        group.setText(LocaleHelper.Application.Group.Activation.Activation);

        Layouts.setGrid(group).numColumns(2).columnsEqualWidth(true).margin(0);

        return group;
    }

    @NotNull
    private Group groupPlayer(Composite composite) {

        Label label = null;
        Button button = null;

        Group group = new Group(composite, SWT.NONE);
        group.setText(LocaleHelper.Application.Group.Sequence.Sequence);

        Layouts.setGrid(group).numColumns(2).columnsEqualWidth(false).margin(0);

        label = new Label(group, SWT.NONE);
        label.setText(LocaleHelper.Application.Group.Sequence.Sequence);

        sequenceCombo = new Combo(group, SWT.NONE);

        Layouts.setGridData(sequenceCombo).grabHorizontal();

        label = new Label(group, SWT.NONE);
        label.setText("Numero video");

        label = new Label(group, SWT.NONE);
        label.setText("3");

        label = new Label(group, SWT.NONE);
        label.setText("Durata");

        label = new Label(group, SWT.NONE);
        label.setText("00:00:09,000");

        label = new Label(group, SWT.NONE);
        label.setText("Nome file");

        label = new Label(group, SWT.NONE);
        label.setText("default.xsq");

        Composite buttonsComposite = new Composite(group, SWT.NONE);
        Layouts.setGrid(buttonsComposite).numColumns(3).columnsEqualWidth(true);
        Layouts.setGridData(buttonsComposite).horizontalSpan(2).grabHorizontal();

        stopButton = new Button(buttonsComposite, SWT.PUSH);
        stopButton.setText("Stop");
        Layouts.setGridData(stopButton).grabHorizontal().minimumWidth(SwtMisc.defaultButtonWidth());
        SwtRx.addListener(stopButton, SWT.Selection).subscribe(evt -> {
            log.info("getIndex() [{}] - stop  pressed ..", getIndex());
            ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                .onNext(MonitorMediaEvent.builder()
                        .monitorId(getIndex())
                        .mediaEvent(new StopCommandEvent())
                        .build()
                );
//          commandEventSubject.onNext(new StopCommandEvent());

            statusEnumRxBox.set(StatusEnum.STOPPED);
        });

        pauseButton = new Button(buttonsComposite, SWT.TOGGLE);
        pauseButton.setText("Pause");
        Layouts.setGridData(pauseButton).grabHorizontal().minimumWidth(SwtMisc.defaultButtonWidth());
        SwtRx.addListener(pauseButton, SWT.Selection).subscribe(evt -> {
            log.info("getIndex() [{}] - pause pressed -  pauseButton.getSelection() {}", getIndex(),
                    pauseButton.getSelection());

            if (pauseButton.getSelection()) {
                ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                    .onNext(MonitorMediaEvent.builder()
                            .monitorId(getIndex())
                            .mediaEvent( new PauseCommandEvent(null))
                            .build()
                    );
                //commandEventSubject.onNext(new PauseCommandEvent(null));
                statusEnumRxBox.set(StatusEnum.PAUSED);
            } else {
                ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                    .onNext(MonitorMediaEvent.builder()
                            .monitorId(getIndex())
                            .mediaEvent(new ResumeCommandEvent(null))
                            .build()
                    );
                // commandEventSubject.onNext(new ResumeCommandEvent(null));
                statusEnumRxBox.set(StatusEnum.PLAYING);
            }
        });

        playButton = new Button(buttonsComposite, SWT.PUSH);
        playButton.setText("Play");
        Layouts.setGridData(playButton).grabHorizontal().minimumWidth(SwtMisc.defaultButtonWidth());
        SwtRx.addListener(playButton, SWT.Selection).subscribe(evt -> {
            log.info("getIndex() [{}] - play pressed ..", getIndex());
            statusEnumRxBox.set(StatusEnum.PLAYING);
        });

        Group mediaGroup = new Group(group, SWT.NONE);
        mediaGroup.setText("current media");
        Layouts.setGrid(mediaGroup).numColumns(2).columnsEqualWidth(false)
                // .margin(2)
                .spacing(2);
        Layouts.setGridData(mediaGroup).horizontalSpan(2).grabHorizontal();

        label = new Label(mediaGroup, SWT.NONE);
        label.setText("Numero video");

        label = new Label(mediaGroup, SWT.NONE);
        label.setText("3");

        label = new Label(mediaGroup, SWT.NONE);
        label.setText("Durata");

        label = new Label(mediaGroup, SWT.NONE);
        label.setText("00:00:09,000");

        label = new Label(mediaGroup, SWT.NONE);
        label.setText("Nome file");

        labelNomeFile = new Label(mediaGroup, SWT.NONE);
        labelNomeFile.setText("");
        Layouts.setGridData(labelNomeFile).grabHorizontal();

        progressBar = new ProgressBar(mediaGroup, SWT.NONE);

        Layouts.setGridData(progressBar).horizontalSpan(2).grabHorizontal();
        progressBar.setMinimum(0);
        progressBar.setMaximum(100);

        return group;
    }

    /**
     *
     */
    private void check() {
        log.debug("getIndex() [{}] - running [{}] - status [{}] - getScreen().isTimed() [{}] ", getIndex(),
                getRunning(), getStatus(), getScreen().isTimed());

        if (StatusEnum.STOPPED.equals(getStatus())) {
            return;
        }

        if (!screen.isTimed()) {
            if (RunningEnum.N.equals(getRunning())
                    || !(StatusEnum.PLAYING.equals(getStatus()) || StatusEnum.PAUSED.equals(getStatus()))) {
                ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                    .onNext(MonitorMediaEvent.builder()
                            .monitorId(getIndex())
                            .mediaEvent( new StartCommandEvent())
                            .build()
                    );
                //commandEventSubject.onNext(new StartCommandEvent());
            }
        }

        if (screen.isTimed()) {

            LocalTime now = LocalTime.now();

            if (RunningEnum.Y.equals(getRunning())) {
                log.debug(
                        "*** getIndex() [{}] - now [{}] getStatus() [{}] getScreen().getFrom() [{}]  getScreen().getTo() [{}]",

                        getIndex(), now, getStatus(), getScreen().getFrom(), getScreen().getTo());

                switch (getStatus()) {
                    case PAUSED:
                    case PLAYING:
                        if ((now.isBefore(getScreen().getFrom()) || now.isAfter(getScreen().getTo()))) {
                            log.warn(
                                    "*** getIndex() [{}] - it's time to deactivate : now [{}]  getStart() [{}] getEnd() [{}]",
                                    getIndex(), now, getScreen().getFrom(), getScreen().getTo());
                            ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                                .onNext(MonitorMediaEvent.builder()
                                        .monitorId(getIndex())
                                        .mediaEvent( new DeactivateCommandEvent())
                                        .build()
                                );
                            //commandEventSubject.onNext(new DeactivateCommandEvent());
                        }
                        break;

                    case NOT_ACTIVE:
                        if (getScreen().getFrom().isBefore(now) && now.isBefore(getScreen().getTo())) {
                            log.warn(
                                    "*** getIndex() [{}] - it's time to   activate :  now [{}]  getStart() [{}] getEnd() [{}]",
                                    getIndex(), now, getScreen().getFrom(), getScreen().getTo());
                            ApplicationUI.MONITOR_MEDIA_EVENT_BUS
                                .onNext(MonitorMediaEvent.builder()
                                            .monitorId(getIndex())
                                            .mediaEvent(new StartCommandEvent())
                                            .build()
                                );

                            //commandEventSubject.onNext(new StartCommandEvent());
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
        log.warn("getIndex() [{}] - disposing tabItemMonitorUI ", getIndex());
//      this.commandEventSubject.onComplete();
//      this.mediaEventSubject.onComplete();
        if (scheduledFuture != null)
            scheduledFuture.cancel(true);

        this.endedProgressMediaEventDisposable.dispose();
        this.monitorUI.dispose();
        super.dispose();
    }

    @Getter
    @Setter
    private StatusEnum status = StatusEnum.NOT_ACTIVE;

    public enum StatusEnum {
        NOT_ACTIVE, STOPPED, PLAYING, PAUSED
    }

    public enum RunningEnum {
        N, Y
    }

    @Getter
    @Setter
    private RunningEnum running = RunningEnum.N;

}
