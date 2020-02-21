package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtExec;
import com.diffplug.common.swt.SwtRx;
import com.diffplug.common.swt.jface.Actions;
import com.diffplug.common.swt.jface.ImageDescriptors;
import com.diffplug.common.swt.jface.JFaceRx;
import io.micronaut.context.annotation.Value;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.helper.SWTHelper;
import me.antoniocaccamo.player.rx.model.preference.MonitorModel;
import me.antoniocaccamo.player.rx.model.preference.PreferenceModel;
import me.antoniocaccamo.player.rx.service.PreferenceService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.IOException;

//import picocli.CommandLine;

/**
 * @author antoniocaccamo on 20/02/2020
 */
@Singleton @Slf4j
public class MainUI {

    @Value("${micronaut.application.name}")
    @NotNull
    private String appname;

    @Value("${micronaut.server.port}")
    @NotNull
    private int port;

    @Inject
    private PreferenceService preferenceService;

    private PreferenceModel preference;

    private PublishSubject<MonitorModel> monitorPublishSubject;

    private CTabFolder tabFolder;

    public void show() {
        preference = preferenceService.read();
        monitorPublishSubject = PublishSubject.create();

        log.info("launching swt .. ");

        Shells shells = Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE, cmp -> {
            Layouts.setGrid(cmp)
                    .numColumns(1)
                    .columnsEqualWidth(true)
                    .horizontalSpacing(0)
                    .verticalSpacing(0)
            ;

            toolbarManager(cmp);
            tabFolder = new CTabFolder(cmp, SWT.NONE);
            Layouts.setGrid(tabFolder);
            Layouts.setGridData(tabFolder).grabAll();

            new TabItemMonitorUI(tabFolder, 0);
            new TabItemMonitorUI(tabFolder, 1);

            SwtRx.addListener(cmp, SWT.Resize, SWT.Move)
                    .subscribe(event -> log.info("event : {} | cmp size : {} location : {}", event, preference.getSize().fromPoint(cmp.getSize()), preference.getLocation().fromPoint(cmp.getLocation())));
            menuManager((Shell) cmp);
            try {
                ((Shell) cmp).setImage(
                        ImageDescriptors.createManagedImage(
                                SWTHelper.getImage( getClass().getClassLoader().getResourceAsStream("images/logo.jpg")).getImageData(),
                                cmp
                        ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        })
        .setTitle(String.format("%s : %s", appname,preference.getComputer()))
        .setSize( preference.getSize().toPoint())
        .setLocation( preference.getLocation().toPoint() )
        ;

        shells.openOnDisplayBlocking();
        log.info("shell closed - > preference : {}", preference);
    }

    private void toolbarManager(Composite cmp) {
        ToolBarManager manager = new ToolBarManager();

        final IAction addMonitor = Actions.builder()
            //.setImage(ImageDescriptors.createManagedImage(SWTHelper.getImage("images/logo.jpg").getImageData(), cmp).)
            .setText("Add")
            .setStyle(Actions.Style.PUSH)
            .setListener(event -> new TabItemMonitorUI(tabFolder, tabFolder.getItems().length) )
            .build()
        ;

        final IAction removeMonitor = Actions.builder()
            //.setImage(ImageDescriptors.createManagedImage(SWTHelper.getImage("images/logo.jpg").getImageData(), cmp).)
            .setText("Remove")
            .setStyle(Actions.Style.PUSH)
            .setListener(evt ->
                Observable.fromArray(tabFolder.getItems())
                    .count()
                    .filter(cnt -> cnt > 1)
                    .subscribe( cnt -> tabFolder.getSelection().dispose())
            )
            .build()
        ;

        monitorPublishSubject.count()
                .subscribe( cnt -> removeMonitor.setEnabled( Boolean.valueOf(cnt > 1)));

        manager.add(addMonitor);
        manager.add(removeMonitor);

        manager.createControl(cmp);
    }

    private void menuManager(Shell shell) {
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
