package me.antoniocaccamo.player.rx.ui.library;

import com.diffplug.common.collect.ImmutableList;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.Shells;
import com.diffplug.common.swt.SwtRx;
import com.diffplug.common.swt.jface.ColumnViewerFormat;

import com.diffplug.common.swt.jface.ViewerMisc;
import io.reactivex.Observable;
import me.antoniocaccamo.player.rx.event.resource.AddMediaEvent;
import me.antoniocaccamo.player.rx.event.resource.ModifyMediaEvent;
import me.antoniocaccamo.player.rx.event.resource.SelecteResourceEvent;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Application;
import me.antoniocaccamo.player.rx.ApplicationUI;
import me.antoniocaccamo.player.rx.model.preference.LoadedSequence;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.service.ResourceService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author ConsCaccamoAntonio on 04/09/2020
 */
@Slf4j
public class SequenceLibraryUITabLoadedSequence extends CTabItem {

    private final LoadedSequence loadedSequence;

    public SequenceLibraryUITabLoadedSequence(final CTabFolder parent, final LoadedSequence loadedSequence) {
        super(parent, SWT.NONE);
        this.loadedSequence = loadedSequence;

        setText(loadedSequence.getName());

        setControl(new Composite(parent, SWT.NONE));
        final Composite composite = (Composite) getControl();
        Layouts.setFill(composite);
        // Layouts.setGridData(composite).grabAll();

        final ColumnViewerFormat<Media> format = ColumnViewerFormat.builder();
        format.addColumn().setText("hash").setLabelProviderText(m -> m.getResourceHash());
        format.addColumn().setText("location").setLabelProviderText(m -> m.getClass().getSimpleName());
        format.addColumn().setText("type").setLabelProviderText(m -> m.getResource().getType().name());
        format.addColumn().setText("duration").setLabelProviderText(r -> r.getDuration() != null ? r.getDuration().toString() : "");
        format.addColumn().setText("path").setLabelProviderText(r -> r.getPath());
        format.setStyle(SWT.FULL_SELECTION | SWT.MULTI);

        final TableViewer tableViewer = format.buildTable(new Composite(composite, SWT.BORDER));
        tableViewer.setContentProvider(new ArrayContentProvider());

        // Layouts.setGridData(tableViewer.getControl()).grabAll();

        tableViewer.setInput(loadedSequence.getSequence().getMedias());

        tableViewer.addDoubleClickListener(new IDoubleClickListener() {

            @Override
            public void doubleClick(DoubleClickEvent event) {
                // TODO Auto-generated method stub

            }
        });

        SwtRx.addListener(tableViewer.getTable(), SWT.MouseDoubleClick).subscribe(evt -> {
            if (tableViewer.getStructuredSelection().size() == 1) {
                Media media = (Media) tableViewer.getStructuredSelection().toList().get(0);
                SequenceLibraryUITabLoadedSequence.modifyMedia(loadedSequence, media);
                // Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE,
                // cmp -> SequenceLibraryUITabLoadedSequence.modifyMedia(cmp, media))
                // .setTitle(String.format("Modify media %s", media.toString())).setSize(300,
                // 400)
                // .setImage(ApplicationUI.ImageUI).openOnActiveBlocking();
            }
        });

        final DropTarget dt = new DropTarget(tableViewer.getControl(), DND.DROP_MOVE);
        dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
        dt.addDropListener(new DropTargetAdapter() {
            public void drop(final DropTargetEvent event) {
                // Set the buttons text to be the text being dropped
                log.warn("## drop : {} sequence name : {}", event.data, loadedSequence.getName());
                final ResourceService resourceService = Application.CONTEXT.getBean(ResourceService.class);
                resourceService.getResourceByHash((String) event.data)
                        .ifPresent(resource -> SequenceLibraryUITabLoadedSequence.addResource(loadedSequence, resource));
            }
        });

        ApplicationUI.RESOURCE_EVENT_BUS
            .filter(ree -> ree instanceof SelecteResourceEvent)
            .subscribe( ree -> {
                log.warn("### selected resource : {}", ree.getResource());
                List<Media> medias = Observable.fromIterable(loadedSequence.getSequence().getMedias())
                        .filter(media -> ree.getResource().equals(media.getResource()))
                        .toList().blockingGet();
                ViewerMisc.multiSelectionList(tableViewer).set(ImmutableList.builder().addAll(medias).build());
               });

    }

    private static void addResource(final LoadedSequence loadedSequence, final Resource resource) {
/*
        Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE,
                cmp -> Layouts.setGrid(cmp).numColumns(1).columnsEqualWidth(true).horizontalSpacing(0)
                        .verticalSpacing(0))
                .setTitle(String.format("Add resource %s", resource.toString())).setSize(300, 400)
                .setImage(ApplicationUI.ImageUI).openOnActiveBlocking();
*/
        ApplicationUI.RESOURCE_EVENT_BUS.onNext( new AddMediaEvent(loadedSequence.getSequence(), resource));
    }

    private static void modifyMedia(final LoadedSequence loadedSequence, final Media media) {
/*
        Shells.builder(SWT.RESIZE | SWT.ICON | SWT.CLOSE,
                cmp -> Layouts.setGrid(cmp).numColumns(1).columnsEqualWidth(true).horizontalSpacing(0)
                        .verticalSpacing(0))
                .setTitle(String.format("Modify resource %s", media.toString())).setSize(300, 400)
                .setImage(ApplicationUI.ImageUI).openOnActiveBlocking();
*/
        ApplicationUI.RESOURCE_EVENT_BUS.onNext( new ModifyMediaEvent(loadedSequence.getSequence(), media.getResource()));
    }

    private static void modifyMedia(final Composite cmp, final Media media) {
        Layouts.setGrid(cmp).numColumns(1).columnsEqualWidth(true).horizontalSpacing(0).verticalSpacing(0);
    }
}
