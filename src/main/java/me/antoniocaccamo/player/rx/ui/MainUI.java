package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtMisc;
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
import me.antoniocaccamo.player.rx.service.TranscodeService;
import me.antoniocaccamo.player.rx.ui.monitor.BrowserUI;
import me.antoniocaccamo.player.rx.ui.monitor.MonitorUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import picocli.CommandLine;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.IOException;

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


    public void show() {
        // business logic here


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

            BrowserUI browser = new BrowserUI(cmp);


            Layouts.setGridData(browser)
                    .grabHorizontal()
                    .grabVertical()
            ;
            browser.getBrowser().setUrl(String.format("http://localhost:%s/hls", port));

            SwtRx.addListener(cmp, SWT.Resize, SWT.Move)
                    .subscribe(event -> log.info("event : {} | cmp size : {} location : {}", event, cmp.getSize(), cmp.getLocation()));


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



            monitorPublishSubject
                    .subscribe(
                            mnt -> Shells.builder(SWT.RESIZE | SWT.READ_ONLY | SWT.ON_TOP, bcmp -> {
                                Layouts.setGrid(bcmp)
                                        .numColumns(1)
                                        .columnsEqualWidth(true)
                                        .horizontalSpacing(0)
                                        .verticalSpacing(0);
                                MonitorUI monitorUI = new MonitorUI(bcmp);

                                SwtRx.addListener(bcmp, SWT.Resize, SWT.Move)
                                        .subscribe(event -> log.info("bcmp[{}] size : {} location : {}", mnt, bcmp.getSize(), bcmp.getLocation()));
                            }).setTitle(mnt.toString())
                                    .setSize(mnt.getSize().toPoint())
                                    .setLocation(mnt.getLocation().toPoint())
                                    .openOn(cmp.getShell()),
                            throwable -> SwtMisc.blockForError(((Shell) cmp).getText(), throwable.getMessage())
                    );

            preference.getMonitors().stream().forEach(monitorModel -> monitorPublishSubject.onNext(monitorModel));

        })
                .setTitle(String.format("%s : %s", appname,preference.getComputer()))
                .setSize( preference.getSize().toPoint())
                .setLocation( preference.getLocation().toPoint() )
                ;

        shells.openOnDisplayBlocking();
        log.info("shell closed");
    }

    private void toolbarManager(Composite cmp) {
        ToolBarManager manager = new ToolBarManager();

        final IAction addMonitor = Actions.builder()
                //.setImage(ImageDescriptors.createManagedImage(SWTHelper.getImage("images/logo.jpg").getImageData(), cmp).)
                .setText("Add")
                .setStyle(Actions.Style.PUSH)
                .setRunnable(() -> {
                    MonitorModel mm = new MonitorModel();
                    preference.getMonitors().add(mm);
                    monitorPublishSubject.onNext(mm);
                })
                .build();

        final IAction removeMonitor = Actions.builder()
                //.setImage(ImageDescriptors.createManagedImage(SWTHelper.getImage("images/logo.jpg").getImageData(), cmp).)
                .setText("Remove")
                .setStyle(Actions.Style.PUSH)
                .setRunnable(() -> log.info("remove"))
                .build();



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
