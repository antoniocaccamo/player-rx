package me.antoniocaccamo.player.rx.ui.monitor;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.ui.ScreenUI;
import org.eclipse.swt.widgets.Composite;

import java.util.concurrent.CountDownLatch;

/**
 * @author antoniocaccamo on 19/02/2020
 */
@Slf4j
public class MonitorVideoUIMonitor extends MonitorBrowserUI {

    public MonitorVideoUIMonitor(ScreenUI screenUI, Composite wrapped, CountDownLatch latch) {
        super(screenUI, wrapped, latch, ShowEnum.VIDEO);
    }


    @Override
    public void setCurrent(Media media) {

        super.setCurrent(media);
    }
}
