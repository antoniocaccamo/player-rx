package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtRx;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.bundle.LocaleManager;
import me.antoniocaccamo.player.rx.model.preference.MonitorModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.*;

import javax.validation.constraints.NotNull;


/**
 * @author antoniocaccamo on 20/02/2020
 */
@Slf4j
public class TabItemMonitorUI extends CTabItem {

    private final int index;
    private final MonitorModel monitorModel;
    private final Shell monitorUI;

    public TabItemMonitorUI(CTabFolder tabFolder, MonitorModel monitorModel,  int index) {
        super(tabFolder, SWT.NONE);
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
            Layouts.setGridData(new MonitorUI(cmp)).grabAll();
        })
        .setSize(300, 280)
        .setLocation(new Point(50,50))
        .openOn(getParent().getShell())
        ;
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

        Group group = new Group(composite, SWT.NONE);
        group.setText(LocaleManager.getText(LocaleManager.Application.Group.Sequence.Sequence));

        Layouts.setGrid(group)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;

        return group;
    }
}
