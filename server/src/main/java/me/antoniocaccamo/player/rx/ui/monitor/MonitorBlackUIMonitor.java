package me.antoniocaccamo.player.rx.ui.monitor;

import me.antoniocaccamo.player.rx.ui.ScreenUI;
import org.eclipse.swt.widgets.Composite;

import java.util.concurrent.CountDownLatch;

/**
 * @author antoniocaccamo on 19/02/2020
 */
public class MonitorBlackUIMonitor extends MonitorBrowserUI {

    public MonitorBlackUIMonitor(ScreenUI screenUI, Composite wrapped, CountDownLatch latch) {
        super(screenUI, wrapped, latch);

    }
}
