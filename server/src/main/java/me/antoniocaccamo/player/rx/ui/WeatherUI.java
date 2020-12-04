package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.Layouts;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * @author antoniocaccamo  on 19/11/2020
 */
@Slf4j
public class WeatherUI extends Composite {
    private final Browser browser;

    public WeatherUI(Composite composite) {
        super(composite, SWT.NONE);
        Layouts.setGrid(this).numColumns(1).columnsEqualWidth(false).margin(0).spacing(0);
        browser = new Browser(this, SWT.NONE);
        Layouts.setGridData(browser).grabAll()
        ;
    }
}
