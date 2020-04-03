package me.antoniocaccamo.player.rx.ui.monitor;

import com.diffplug.common.swt.ColorPool;
import me.antoniocaccamo.player.rx.ui.AbstractUI;
import me.antoniocaccamo.player.rx.ui.MonitorUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author antoniocaccamo on 19/02/2020
 */
public class WeatherUI extends AbstractUI {
    public WeatherUI(MonitorUI monitorUI, Composite wrapped) {
        super(monitorUI, wrapped);
        setBackground(ColorPool.forWidget(this).getSystemColor(SWT.COLOR_BLACK));
    }
}
