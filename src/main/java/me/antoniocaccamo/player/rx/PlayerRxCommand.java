package me.antoniocaccamo.player.rx;

import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtRx;
import com.diffplug.common.swt.jface.Actions;
import com.diffplug.common.swt.jface.JFaceRx;
import com.diffplug.common.swt.widgets.ButtonPanel;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Value;
import io.micronaut.runtime.Micronaut;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.MainViewModel;
import me.antoniocaccamo.player.rx.service.PreferenceService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import static org.eclipse.swt.SWT.BAR;
import static org.eclipse.swt.SWT.getPlatform;

@Command(name = "player-rx", description = "...",
        mixinStandardHelpOptions = true)
@Slf4j
public class PlayerRxCommand implements Runnable {

    @Value("${micronaut.application.name}") @NotNull
    private String appname;


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

        Shells shells =  Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE, cmp -> {
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



                menuManager((Shell) cmp);

        })
                .setTitle(String.format("%s : %s", appname,mainViewModel.getComputer()))

                .setSize( PlayerRxCommand.toPoint(  mainViewModel.getSize().getWidth(), mainViewModel.getSize().getHeight()) )
                .setLocation(PlayerRxCommand.toPoint(  mainViewModel.getLocation().getLeft(), mainViewModel.getLocation().getTop()) );
        shells.openOnDisplayBlocking();


    }

    private void menu(Shell shell){
        // create the menu
        Menu m = new Menu(shell,SWT.BAR);

        // create a File menu and add an Exit item
        final MenuItem file = new MenuItem(m, SWT.CASCADE);
        file.setText("&File");

        final Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
        file.setMenu(filemenu);
        final MenuItem openMenuItem = new MenuItem(filemenu, SWT.PUSH);
        openMenuItem.setText("&Open\tCTRL+O");
        openMenuItem.setAccelerator(SWT.CTRL+'O');
        final MenuItem saveMenuItem = new MenuItem(filemenu, SWT.PUSH);
        saveMenuItem.setText("&Save\tCTRL+S");
        saveMenuItem.setAccelerator(SWT.CTRL+'S');
        final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
        final MenuItem exitMenuItem = new MenuItem(filemenu, SWT.PUSH);
        exitMenuItem.setText("E&xit");

        // create an Edit menu and add Cut, Copy, and Paste items
        final MenuItem edit = new MenuItem(m, SWT.CASCADE);
        edit.setText("&Edit");
        final Menu editmenu = new Menu(shell, SWT.DROP_DOWN);
        edit.setMenu(editmenu);
        final MenuItem cutMenuItem = new MenuItem(editmenu, SWT.PUSH);
        cutMenuItem.setText("&Cut");
        final MenuItem copyMenuItem = new MenuItem(editmenu, SWT.PUSH);
        copyMenuItem.setText("Co&py");
        final MenuItem pasteMenuItem = new MenuItem(editmenu, SWT.PUSH);
        pasteMenuItem.setText("&Paste");

        //create a Window menu and add Child items
        final MenuItem window = new MenuItem(m, SWT.CASCADE);
        window.setText("&Window");
        final Menu windowmenu = new Menu(shell, SWT.DROP_DOWN);
        window.setMenu(windowmenu);
        final MenuItem maxMenuItem = new MenuItem(windowmenu, SWT.PUSH);
        maxMenuItem.setText("Ma&ximize");
        final MenuItem minMenuItem = new MenuItem(windowmenu, SWT.PUSH);
        minMenuItem.setText("Mi&nimize");

        // create a Help menu and add an About item
        final MenuItem help = new MenuItem(m, SWT.CASCADE);
        help.setText("&Help");
        final Menu helpmenu = new Menu(shell, SWT.DROP_DOWN);
        help.setMenu(helpmenu);
        final MenuItem aboutMenuItem = new MenuItem(helpmenu, SWT.PUSH);
        aboutMenuItem.setText("&About");

        shell.setMenuBar(m);
    }


    private void menuManager(Shell shell){
        MenuManager manager = new MenuManager();

        MenuManager file_menu = new MenuManager("&File");
        IAction action = Actions.builder()
                .setText("Action")
                .setStyle(Actions.Style.CHECK)
                .setRunnable(() -> log.info("ciao"))
                .build();

        RxBox<Boolean> selection = JFaceRx.toggle(action);
        file_menu.add(action);



        selection.set(Boolean.TRUE);

        manager.add(file_menu);

        Menu menu = manager.createMenuBar((Decorations) shell);

        shell.setMenuBar(menu);
    }
}
