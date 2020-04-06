package me.antoniocaccamo.player.rx.ui.monitor;

import com.diffplug.common.swt.Layouts;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.ui.MonitorUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @author antoniocaccamo on 19/02/2020
 */
@Slf4j
public class PhotoUI extends BrowserUI {

    public PhotoUI(MonitorUI monitorUI, Composite wrapped) {
        super(monitorUI, wrapped);

        Label label = new Label(this, SWT.NONE);
        label.setText(getClass().getSimpleName());
        Layouts.setGridData(label).grabAll();
    }

}
