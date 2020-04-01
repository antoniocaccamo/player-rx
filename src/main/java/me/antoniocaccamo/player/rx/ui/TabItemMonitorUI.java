package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.*;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Main;
import me.antoniocaccamo.player.rx.bundle.LocaleManager;
import me.antoniocaccamo.player.rx.event.media.command.CommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.PauseCommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.PlayCommandEvent;
import me.antoniocaccamo.player.rx.event.media.command.StopCommandEvent;
import me.antoniocaccamo.player.rx.event.media.progress.MediaEvent;
import me.antoniocaccamo.player.rx.model.preference.MonitorModel;
import me.antoniocaccamo.player.rx.service.SequenceService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.*;

import javax.validation.constraints.NotNull;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * @author antoniocaccamo on 20/02/2020
 */
@Slf4j
public class TabItemMonitorUI extends CTabItem {

    private final int index;
    private final MonitorModel monitorModel;
    private final Shell monitorUI;

    private final SequenceService sequenceService;

    // tab -> monitor
    private final PublishSubject<CommandEvent> commandEventSubject = PublishSubject.create();

    // monitor -> tab
    private final PublishSubject<MediaEvent> mediaEventSubject = PublishSubject.create();

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

        monitorUI = Shells.builder(SWT.BORDER, cmp -> {
            Layouts.setGrid(cmp);
            Layouts.setGridData(new MonitorUI(cmp, commandEventSubject, mediaEventSubject)).grabAll();
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
                
        SwtExec.async().guardOn(this)
                .scheduleAtFixedRate(()->log.debug("===>>> player #{}: check to run/stop...", this.index), 100L, 500L, TimeUnit.MILLISECONDS);

        this.mediaEventSubject.subscribe( mediaEvent -> log.info("media event received : {}", mediaEvent));
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

        button = new Button(buttonsComposite, SWT.PUSH);
        button.setText("Stop");
        Layouts.setGridData(button)
                .grabHorizontal()
                .minimumWidth(SwtMisc.defaultButtonWidth())
        ;
        SwtRx.addListener(button, SWT.Selection).subscribe(evt -> {
            log.info("stop  pressed ..");
            commandEventSubject.onNext( new StopCommandEvent(null));
        });

        button = new Button(buttonsComposite, SWT.PUSH);
        button.setText("Pause");
        Layouts.setGridData(button)
                .grabHorizontal()
                .minimumWidth(SwtMisc.defaultButtonWidth())
        ;
        SwtRx.addListener(button, SWT.Selection).subscribe(evt ->{
            log.info("pause pressed ..");
            commandEventSubject.onNext( new PauseCommandEvent(null));
        });

        button = new Button(buttonsComposite, SWT.PUSH);
        button.setText("Play");
        Layouts.setGridData(button)
                .grabHorizontal()
                .minimumWidth(SwtMisc.defaultButtonWidth())
        ;
        SwtRx.addListener(button, SWT.Selection).subscribe(evt ->{
            log.info("play pressed ..");
            commandEventSubject.onNext( new PlayCommandEvent(null)) ;
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


        Layouts.setGridData( new ProgressBar(mediaGroup, SWT.NONE))
                .horizontalSpan(2)
                .grabHorizontal()


        ;



        return group;
    }

    @Override
    public void dispose() {
        monitorUI.dispose();
        super.dispose();
    }
}
