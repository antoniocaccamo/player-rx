package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.*;
import com.diffplug.common.swt.jface.Actions;
import com.diffplug.common.swt.jface.ImageDescriptors;
import io.micronaut.context.annotation.Value;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.helper.DBInitHelper;
import me.antoniocaccamo.player.rx.helper.SWTHelper;
import me.antoniocaccamo.player.rx.model.preference.LocationModel;
import me.antoniocaccamo.player.rx.model.preference.MonitorModel;
import me.antoniocaccamo.player.rx.model.preference.PreferenceModel;
import me.antoniocaccamo.player.rx.model.preference.SizeModel;
import me.antoniocaccamo.player.rx.service.PreferenceService;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Decorations;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Inject
    private DBInitHelper dbInitHelper;

    private PreferenceModel preference;

    private PublishSubject<MonitorModel> monitorPublishSubject;

    private CTabFolder tabFolder;

    private CoatMux coatMux;

    private AtomicInteger tabFolderIndex;

    private CoatMux.Layer<Composite> tabFolderLayer;
    private CoatMux.Layer<Composite> resourceLibraryLayer;

    @PostConstruct
    public void show() {

        dbInitHelper.getDefaultSquence();
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
            coatMux = new CoatMux(cmp, SWT.NONE);
            Layouts.setGridData(coatMux).grabAll();

            tabFolderLayer = coatMux.addCoat(composite -> {
                Layouts.setGrid(composite)
                        .numColumns(1)
                        .columnsEqualWidth(true)
                        .horizontalSpacing(0)
                        .verticalSpacing(0)
                ;
                tabFolder = new CTabFolder(composite, SWT.NONE);
                Layouts.setGrid(tabFolder);
                Layouts.setGridData(tabFolder).grabAll();
                return composite;
            });

            resourceLibraryLayer = coatMux.addCoat(cmpResLib ->{
                Layouts.setGrid(cmpResLib).horizontalSpacing(0).verticalSpacing(0);
                Layouts.setGridData(new ResourceLibraryUI(cmpResLib)).grabAll();
                return cmpResLib;
            });

            tabFolderLayer.bringToTop();

            tabFolderIndex = new AtomicInteger(0);
            preference.getMonitors().stream()
                .forEach( monitorModel -> new TabItemMonitorUI(tabFolder, monitorModel, tabFolderIndex.getAndIncrement()) );

            SwtRx.addListener(cmp, SWT.Resize, SWT.Move)
                .subscribe(event ->
                        log.debug("event : {} | cmp size : {} location : {}", event, preference.getSize().fromPoint(cmp.getSize()), preference.getLocation().fromPoint(cmp.getLocation()))
                );
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

            SwtRx.addListener(cmp, SWT.Dispose)
                    .subscribe(event -> {
                        for (CTabItem item: tabFolder.getItems()) {
                            item.getControl().dispose();
                            item.dispose();
                        }
                        System.exit(0);
                    });
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
                .setListener(event -> {
                    CTabItem cTabItem = new TabItemMonitorUI(tabFolder,
                        new MonitorModel(
                            new SizeModel(320, 280),
                            new LocationModel( 50, 50),
                            "", "",
                                null, null, Constants.TimingEnum.ALL_DAY

                        )
                        , tabFolderIndex.getAndIncrement());
                    tabFolder.setSelection(cTabItem);
                        })
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
                                .subscribe( cnt -> {
                                    tabFolder.getSelection().dispose();
                                    tabFolderIndex.decrementAndGet();
                                })
                )
                .build()
                ;

        monitorPublishSubject.count()
                .subscribe( cnt -> removeMonitor.setEnabled( Boolean.valueOf(cnt > 1)));

        manager.add(addMonitor);
        manager.add(removeMonitor);
        manager.add(
                Actions.builder()
                        .setListener(event -> tabFolderLayer.bringToTop())
                        .setText("monitors")
                        .build()
        );
        manager.add(
                Actions.builder()
                        .setListener(event -> resourceLibraryLayer.bringToTop())
                        .setText("resource lib")
                        .build()
        );

        manager.createControl(cmp);
    }

    private void menuManager(Shell shell) {
        MenuManager manager = new MenuManager();

        MenuManager file_menu = new MenuManager("&File");

//      Save
        file_menu.add(Actions.builder()
                .setText("&Save")
                .setStyle(Actions.Style.PUSH)
                .setRunnable(() -> {
                    try {
                        preferenceService.save();
                    } catch (IOException e) {
                        log.error("error saving prefs", e);
                    }
                })
                .build()
        );


//      Exit
        file_menu.add(Actions.builder()
                .setText("&Exit")
                .setStyle(Actions.Style.PUSH)
                .setRunnable(() -> {
                        if ( SwtMisc.blockForQuestion("aaaaaa", "exitt ??" ) )
                            System.exit(0);
                })
                .build()
        );


 //       selection.set(Boolean.TRUE);

        manager.add(file_menu);

        Menu menu = manager.createMenuBar((Decorations) shell);

        shell.setMenuBar(menu);
    }


}
