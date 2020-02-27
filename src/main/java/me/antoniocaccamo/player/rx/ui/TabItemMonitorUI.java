package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.CoatMux;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.SwtRx;
import me.antoniocaccamo.player.rx.bundle.LocaleManager;
import me.antoniocaccamo.player.rx.helper.SWTHelper;
import me.antoniocaccamo.player.rx.model.preference.MonitorModel;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import javax.validation.constraints.NotNull;


/**
 * @author antoniocaccamo on 20/02/2020
 */
public class TabItemMonitorUI extends CTabItem {

    private final int index;
    private final MonitorModel monitorModel;

    public TabItemMonitorUI(CTabFolder tabFolder, MonitorModel monitorModel,  int index) {
        super(tabFolder, SWT.NONE);
        setText(String.format("screen %s", index + 1));
        this.monitorModel = monitorModel;
        this.index = index;

        setControl(new Composite(getParent(), SWT.NONE) );
        Composite composite = (Composite) getControl();

        Layouts.setGrid(composite)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;

        Layouts.setGridData(definePlayerGroup(composite))
                .grabHorizontal();

        Layouts.setGridData(defineSequenceGroup(composite))
                .grabHorizontal();
    }

    @NotNull
    private Group definePlayerGroup(Composite composite) {
        Label label     = null;
        Spinner spinner = null;

        final Group group = new Group(composite, SWT.NONE);
        group.setText("size & location");

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
        Layouts.setGridData(sizeGroup).grabAll();
        label = new Label(sizeGroup, SWT.NONE);
        label.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Size.Width));
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(sizeGroup, SWT.NONE);
        spinner.setMinimum(0);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(monitorModel.getSize().getWidth());
        spinner.addModifyListener(e -> monitorModel.getSize().setWidth(((Spinner)e.getSource()).getSelection()));
        label = new Label(sizeGroup, SWT.NONE);
        label.setText(LocaleManager.getText(LocaleManager.Application.Group.Screen.Size.Height));
        Layouts.setGridData(label).grabAll();
        spinner = new Spinner(sizeGroup, SWT.NONE);
        spinner.setMinimum(0);
        Layouts.setGridData(spinner).grabAll();
        spinner.setSelection(monitorModel.getSize().getHeight());
        spinner.addModifyListener(e -> monitorModel.getSize().setHeight(((Spinner)e.getSource()).getSelection()));








        // -- location
        final Group locationGroup = new Group(group, SWT.NONE);
        locationGroup.setText(LocaleManager.getText(LocaleManager.APP_GROUP_SCREEN_LOCATION));
        Layouts.setGrid(locationGroup)
                .numColumns(2)
                .columnsEqualWidth(true)
                .margin(0)
        ;
        Layouts.setGridData(locationGroup).grabAll();


        return group;
    }

    @NotNull
    private Group defineSequenceGroup(Composite composite) {

        Group group = new Group(composite, SWT.NONE);
        group.setText("sequence");
        Layouts.setGrid(group);

        return group;
    }
}
