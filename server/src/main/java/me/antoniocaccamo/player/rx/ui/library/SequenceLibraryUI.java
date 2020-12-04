package me.antoniocaccamo.player.rx.ui.library;

import com.diffplug.common.collect.ImmutableList;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.jface.ColumnViewerFormat;
import com.diffplug.common.swt.jface.ViewerMisc;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Application;
import me.antoniocaccamo.player.rx.ApplicationUI;
import me.antoniocaccamo.player.rx.event.resource.ResourceEvent;
import me.antoniocaccamo.player.rx.event.resource.SelecteResourceEvent;
import me.antoniocaccamo.player.rx.model.preference.LoadedSequence;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.service.SequenceService;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.*;

import java.util.List;

/**
 * @author antoniocaccamo on 05/05/2020
 */
@Slf4j
public class SequenceLibraryUI extends Composite {

    private final SequenceService sequenceService;
    private TreeViewer treeViewer;
    private CTabFolder tabFolder;

    public SequenceLibraryUI(Composite parent/*, PublishSubject<ResourceEvent> resourcePublishSubject*/) {
        super(parent, SWT.NONE);

        Layouts.setGrid(this).numColumns(1).columnsEqualWidth(false).margin(0).spacing(0);

        sequenceService = Application.CONTEXT.getBean(SequenceService.class);

        Group group = new Group(this, SWT.NONE);
        Layouts.setGrid(group).numColumns(1).spacing(0).margin(0);
        //Layouts.setGrid(group);
        //Layouts.setGridData(group).grabAll();
        group.setText("sequences..01");
        Layouts.setGrid(group).numColumns(1);
        Layouts.setGridData(group).grabAll();
        Composite composite = new Composite(group, SWT.NONE);

        //createTreeViewer(group, composite);
        createTabFolder(group, composite);
    }

    private void createTabFolder(Group group, Composite composite) {
        Layouts.setGrid(composite).numColumns(1);
        Layouts.setGridData(composite).grabAll();

        tabFolder = new CTabFolder(composite, SWT.NONE);

        Layouts.setGrid(tabFolder);
        Layouts.setGridData(tabFolder).grabAll();

        Observable.fromIterable(sequenceService.getLoadedSequences())
            .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
            .subscribe(
                loadedSequence -> new SequenceLibraryUITabLoadedSequence(tabFolder, loadedSequence),
                Throwable::printStackTrace
            );
        if ( tabFolder.getItemCount() > 0 )
            tabFolder.setSelection(0);
    }

    @Deprecated
    private void createTabItem(LoadedSequence loadedSequence){
        CTabItem tabItem = new CTabItem(tabFolder, SWT.NONE);
    }

    @Deprecated
    private void createTreeViewer(Group group, Composite composite) {

        Layouts.setFill(composite);
        Layouts.setGridData(composite).grabAll();

        ColumnViewerFormat<LoadedSequence> format = ColumnViewerFormat.builder();
        format.addColumn().setText("name");
        format.addColumn().setText("path");
        format.addColumn().setText("media");
        format.setStyle( SWT. FULL_SELECTION | SWT.MULTI);

        treeViewer = format.buildTree(new Composite(composite, SWT.BORDER));
        treeViewer.setContentProvider( new SequenceLibraryUITreeContentProvider());
        treeViewer.setLabelProvider(   new SequenceLibraryUITreeTableLabelProvider());
//        ViewerMisc.singleSelection(treeViewer)
//                .asObservable()
//                .subscribe(oo -> oo.ifPresent( o-> log.info("selected : {}", o.getClass().getSimpleName())));

        Iterable<LoadedSequence> loadedSequenceIterable = sequenceService.getLoadedSequences();
        treeViewer.setInput(loadedSequenceIterable);

        Composite buttoComposite = new Composite(group, SWT.SHADOW_ETCHED_OUT | SWT.CENTER);
        Layouts.setGrid(buttoComposite).numColumns(3);
        Layouts.setGridData(buttoComposite).grabHorizontal().horizontalAlignment(SWT.CENTER);

        Button button = new Button(buttoComposite, SWT.PUSH );
        button.setText("button 01");

        button = new Button(buttoComposite, SWT.PUSH );
        button.setText("button 02");

        button = new Button(buttoComposite, SWT.PUSH );
        button.setText("button 03");


        ApplicationUI.RESOURCE_EVENT_BUS
            .filter(resourceEvent -> resourceEvent instanceof SelecteResourceEvent)
            .map(resourceEvent -> (SelecteResourceEvent) resourceEvent)
            .subscribe( sre -> {
                Resource resource = sre.getResource();
                List<Object> mediaList = Observable.fromIterable(loadedSequenceIterable)
                        .flatMap( loadedSequence -> Observable.fromIterable(loadedSequence.getSequence().getMedias())
                                        .filter(media -> media.getResource().equals(resource))
                                        .map(media -> (Object) media)

                        )
                        .toList().blockingGet();
                ViewerMisc.multiSelectionList(treeViewer).set(ImmutableList.builder().addAll(mediaList).build());
            });

        DropTarget dt = new DropTarget(treeViewer.getControl(), DND.DROP_MOVE);
        dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
        dt.addDropListener(new DropTargetAdapter() {
            public void drop(DropTargetEvent event) {
                // Set the buttons text to be the text being dropped
                log.warn("## drop : {}", event.data);
            }
        });
    }
}