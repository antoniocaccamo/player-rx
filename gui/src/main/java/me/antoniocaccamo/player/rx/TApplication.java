package me.antoniocaccamo.player.rx;

import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtExec;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author antoniocaccamo  on 05/11/2020
 */
@Slf4j
public class TApplication {

    public static void main(String[] args) {
        Shells.builder(SWT.CLOSE, TApplication::subShells)
                .setRectangle(new Rectangle(50,50, 400, 320))
                .setTitle("\"subshell\"")
                .openOnDisplay();
    }

    private static void subShells(Composite composite) {
        log.info("subshells");

        Executor executor =
                // Executors.newCachedThreadPool()
                   SwtExec.async().getRxExecutor().executor()
                ;

        executor.execute(() -> {
            log.info("subshell 01..");
            Shells.builder(SWT.CLOSE, cmp -> {
                cmp.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        log.info("focus gained");
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        log.info("focus lost");
                    }
                });
            })
            .setRectangle(new Rectangle(150, 150, 400, 320))
            .setTitle("\"subshell 01\"")
            .openOn(composite.getShell());
            log.info("subshell 01 done");
        });

        executor.execute(() -> {
            log.info("subshell 02..");

            Shells.builder(SWT.CLOSE, cmp -> {
                cmp.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        log.info("focus gained");
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        log.info("focus lost");
                    }
                });
            })
            .setRectangle(new Rectangle(250, 250, 400, 320))
            .setTitle("\"subshell 02\"")
            .openOn(composite.getShell());
            log.info("subshell 02");
        });
    }
}
