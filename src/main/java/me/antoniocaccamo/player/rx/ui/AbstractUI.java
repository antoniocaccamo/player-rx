package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.Layouts;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author antoniocaccamo on 19/02/2020
 */
public abstract class AbstractUI extends Composite {

    public AbstractUI(Composite parent) {
        super(parent, SWT.NONE);
        Layouts.setGrid(this)
                .margin(0)
                .numColumns(1)
                .columnsEqualWidth(true)
        ;
        Layouts.setGridData(this)
                .grabAll()
        ;
    }
}
