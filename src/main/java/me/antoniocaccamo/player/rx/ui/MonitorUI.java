package me.antoniocaccamo.player.rx.ui;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.ui.AbstractUI;
import org.eclipse.swt.widgets.Composite;

/**
 * @author antoniocaccamo on 20/02/2020
 */
@Slf4j
public class MonitorUI extends AbstractUI {
    public MonitorUI(Composite wrapped) {
        super(wrapped);
        log.info("...");
    }
}
