package me.antoniocaccamo.player.rx;

import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtRx;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.Micronaut;
import io.micronaut.runtime.server.EmbeddedServer;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.ui.BrowserUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;

import javax.validation.constraints.NotNull;

@Slf4j
public class Application {

    @Value("${micronaut.application.name}")
    @NotNull
    private static String appname;


    @Value("${micronaut.server.port}")
    @NotNull
    private static int port;

    public static void main(String[] args) {
        Micronaut.run(Application.class);

        Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE, cmp -> {
            Layouts.setGrid(cmp).margin(0);

            BrowserUI browserUI = new BrowserUI(cmp);

            browserUI.getBrowser().setUrl(String.format("http://localhost:%s/hls", port));

            SwtRx.addListener(cmp, SWT.Resize, SWT.Move)
                    .subscribe(event -> log.info("event : {} | cmp size : {} location : {}", event, cmp.getSize(), cmp.getLocation()));

        }).setTitle(appname)
                .setSize(386, 280)
                .setLocation(new Point(50,50))
                .openOnDisplayBlocking();

//        try (ApplicationContext context = ApplicationContext.run()) {
//            // do something with your bean
//
//            context.findBean(EmbeddedServer.class)
//                    .ifPresent( srv -> srv.start());
//
//
//            Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE, cmp -> {
//                Layouts.setGrid(cmp).margin(0);
//
//                BrowserUI browserUI = new BrowserUI(cmp);
//
//                browserUI.getBrowser().setUrl(String.format("http://localhost:9000/hls", port));
//
//                SwtRx.addListener(cmp, SWT.Resize, SWT.Move)
//                        .subscribe(event -> log.info("event : {} | cmp size : {} location : {}", event, cmp.getSize(), cmp.getLocation()));
//
//            }).setTitle(appname)
//                    .setSize(386, 280)
//                    .setLocation(new Point(50,50))
//                    .openOnDisplayBlocking();
//
//
//            context.findBean(EmbeddedServer.class)
//                    .ifPresent( srv -> srv.stop());
//        }
    }
}