package me.antoniocaccamo.player.rx.ui.library;

import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.jface.ColumnViewerFormat;
import me.antoniocaccamo.player.rx.model.preference.LoadedSequence;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * @author ConsCaccamoAntonio  on 04/09/2020
 */
public class SequenceLibraryUITabLoadedSequence extends CTabItem {

    private final LoadedSequence loadedSequence;

    public SequenceLibraryUITabLoadedSequence(CTabFolder parent, LoadedSequence loadedSequence) {
        super(parent, SWT.NONE);
        this.loadedSequence = loadedSequence;

        setText(loadedSequence.getName());

        setControl(new Composite(parent, SWT.NONE) );
        Composite composite = (Composite) getControl();
        Layouts.setFill(composite);
        //Layouts.setGridData(composite).grabAll();

        ColumnViewerFormat<Media> format = ColumnViewerFormat.builder();
        format.addColumn().setText("hash").setLabelProviderText(m -> m.getResourceHash());
        format.addColumn().setText("location").setLabelProviderText(m-> m.getClass().getSimpleName());
        format.addColumn().setText("type").setLabelProviderText(m-> m.getResource().getType().name());
        format.addColumn().setText("duration").setLabelProviderText(r->r.getDuration() != null ?r.getDuration().toString(): "");
        format.addColumn().setText("path").setLabelProviderText(r->r.getPath());
        format.setStyle( SWT. FULL_SELECTION);

        TableViewer tableViewer = format.buildTable(new Composite(composite, SWT.BORDER));
        tableViewer.setContentProvider(new ArrayContentProvider());

        //   Layouts.setGridData(tableViewer.getControl()).grabAll();

        tableViewer.setInput(loadedSequence.getSequence().getMedias());

    }
}
