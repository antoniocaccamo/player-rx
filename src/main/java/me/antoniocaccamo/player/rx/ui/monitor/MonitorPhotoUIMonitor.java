package me.antoniocaccamo.player.rx.ui.monitor;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.ui.ScreenUI;
import org.eclipse.swt.widgets.Composite;

import java.util.concurrent.CountDownLatch;

/**
 * @author antoniocaccamo on 19/02/2020
 */
@Slf4j
public class MonitorPhotoUIMonitor extends MonitorBrowserUI {

    public MonitorPhotoUIMonitor(ScreenUI screenUI, Composite wrapped, CountDownLatch latch) {
        super(screenUI, wrapped, latch, ShowEnum.PHOTO);

    }

}
