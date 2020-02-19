package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.ColorPool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author antoniocaccamo on 19/02/2020
 */
public class BlackUI extends AbstractUI {
    public BlackUI(Composite wrapped) {
        super(wrapped);
        setBackground(ColorPool.forWidget(this).getSystemColor(SWT.COLOR_BLACK));
    }
}
