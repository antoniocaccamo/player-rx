package me.antoniocaccamo.player.rx;

import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtRx;
import com.diffplug.common.swt.widgets.ButtonPanel;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.Micronaut;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.MainViewModel;
import me.antoniocaccamo.player.rx.service.PreferenceService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import static org.eclipse.swt.SWT.BAR;

@Command(name = "player-rx", description = "...",
        mixinStandardHelpOptions = true)
@Slf4j
public class PlayerRxCommand implements Runnable {

    @Value("${micronaut.server.port}") @NotNull
    private int port;

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    @Inject
    private PreferenceService preferenceService;

    public static void main(String[] args) throws Exception {
        try ( ApplicationContext context= Micronaut.run(PlayerRxCommand.class, args) ) {
            PicocliRunner.run(PlayerRxCommand.class, context, args);
        }
    }

    public static Point toPoint(@NotNull  int i1, @NotNull  int i2){
        return new Point(i1, i2);
    }

    public void run() {
        // business logic here
        if (verbose) {
            log.info("Hi!");
        }

        MainViewModel mainViewModel = preferenceService.read();

        log.info("launching swt .. ");

        Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE, cmp -> {
            Layouts.setGrid(cmp)
                    .numColumns(1)

                    .columnsEqualWidth(true)
                    .horizontalSpacing(0)
                    .verticalSpacing(0)
            ;

            Browser browser = new Browser(cmp, SWT.NONE);
            Layouts.setGridData(browser)
                    .grabHorizontal()
                    .grabVertical()
            ;
            browser.setUrl(String.format("http://localhost:%s/hls", port));

            SwtRx.addListener(cmp, SWT.Resize, SWT.Move)
                    .subscribe(event -> log.info("event : {} | cmp size : {} location : {}", event, cmp.getSize(), cmp.getLocation()));



            Menu menuBar = new Menu(browser);
            MenuItem fileMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
            fileMenuHeader.setText("&File");

            Menu fileMenu = new Menu(cmp.getShell(), SWT.DROP_DOWN);
            fileMenuHeader.setMenu(fileMenu);

            MenuItem fileSaveItem = new MenuItem(fileMenu, SWT.PUSH);
            fileSaveItem.setText("&Save");

            MenuItem fileExitItem = new MenuItem(fileMenu, SWT.PUSH);
            fileExitItem.setText("E&xit");

            MenuItem helpMenuHeader = new MenuItem(menuBar, SWT.CASCADE);
            helpMenuHeader.setText("&Help");

            Menu helpMenu = new Menu(cmp.getShell(), SWT.DROP_DOWN);
            helpMenuHeader.setMenu(helpMenu);

            MenuItem helpGetHelpItem = new MenuItem(helpMenu, SWT.PUSH);
            helpGetHelpItem.setText("&Get Help");

        })
                .setSize( PlayerRxCommand.toPoint(  mainViewModel.getSize().getWidth(), mainViewModel.getSize().getHeight()) )
                .setLocation(PlayerRxCommand.toPoint(  mainViewModel.getLocation().getLeft(), mainViewModel.getLocation().getTop()) )
                .openOnDisplayBlocking();

        
    }
}
