package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.CoatMux;
import com.diffplug.common.swt.Layouts;
import me.antoniocaccamo.player.rx.helper.SWTHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;


/**
 * @author antoniocaccamo on 20/02/2020
 */
public class TabItemMonitorUI extends CTabItem {

    private final int index;

    public TabItemMonitorUI(CTabFolder tabFolder, int index) {
        super(tabFolder, SWT.NONE, index);
        setText(String.format("screen %s", index + 1));
        this.index = index;
        setControl(new Composite(getParent(), SWT.NONE) );
        Composite composite = (Composite) getControl();
        Layouts.setGrid(composite).numColumns(2).columnsEqualWidth(true).margin(0);


        Layouts.setGridData(definePlayerGroup(composite))
                .grabAll();

        Layouts.setGridData(defineSequenceGroup(composite))
                .grabAll();


    }

    private Group definePlayerGroup(Composite composite) {

        Group group = new Group(composite, SWT.NONE);
        group.setText("player");

        Layouts.setGrid(group);

        return group;
    }

    private Group defineSequenceGroup(Composite composite) {

        Group group = new Group(composite, SWT.NONE);
        group.setText("sequence");
        Layouts.setGrid(group);

        return group;
    }
}
