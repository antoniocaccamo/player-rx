package me.antoniocaccamo.player.rx.ui.monitor;

import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.SwtExec;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Main;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.ui.MonitorUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        final String url = String.format("http://localhost:%s/html/ui/photo/", Main.port);
        log.info("getIndex() [{}] - url  : {}", this.getMonitorUI().getIndex(), url);
        browser.setUrl( url );
    }

    @Override
    public void play() {

        final Path path = this.current.getResource().getLocalPath();
        log.info("getIndex() [{}] - show photo  : {}", this.getMonitorUI().getIndex(), path);
        SwtExec.async().guardOn(browser)
                .execute(() ->
                        log.info("getIndex() [{}] - execute {} - result : {}"    ,
                                getMonitorUI().getIndex(),
                                String.format("load(%s?%s)", path, startInMillis),
                                browser.execute(String.format("load(%s?%s)", path, startInMillis))
                        )
                );
        super.play();
    }
}
