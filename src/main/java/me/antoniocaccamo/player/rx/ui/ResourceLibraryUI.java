package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.Layouts;
import com.diffplug.common.swt.jface.ColumnViewerFormat;
import com.diffplug.common.swt.jface.ViewerMisc;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Application;
import me.antoniocaccamo.player.rx.model.resource.AbstractResource;
import me.antoniocaccamo.player.rx.service.ResourceService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import java.util.Optional;

/**
 * @author antoniocaccamo on 02/03/2020
 */
@Slf4j
public class ResourceLibraryUI extends Composite {

    public ResourceLibraryUI(Composite parent) {
        super(parent, SWT.VERTICAL);


        Layouts.setFill(this);

//        SashForm sashForm = new SashForm(this, SWT.HORIZONTAL);
//
//
//        Button button2 = new Button(sashForm, SWT.NONE);
//        button2.setText("Button 2");
//
//        Button button3 = new Button(sashForm, SWT.NONE);
//        button3.setText("Button 3");
//       sashForm.setWeights(new int[] { 2, 1});


        Group group = new Group(this, SWT.NONE);
        group.setText("resources");
        //Layouts.setFill(group);

        ColumnViewerFormat<AbstractResource> format = ColumnViewerFormat.builder();
        format.addColumn().setText("location").setLabelProviderText(r-> r.getLocation().name());
        format.addColumn().setText("type").setLabelProviderText(r-> r.getType().name());
        format.addColumn().setText("duration").setLabelProviderText(r->r.getDuration() != null ?r.getDuration().toString(): "");
        format.addColumn().setText("path").setLabelProviderText(r->r.getPath());
        format.setStyle(SWT.BORDER | SWT. FULL_SELECTION);

        TableViewer tableViewer = format.buildTable(group);
        tableViewer.setContentProvider(new ArrayContentProvider());
        RxBox<Optional<AbstractResource>> resourceRxBox  = ViewerMisc.singleSelection(tableViewer);
        resourceRxBox.asObservable().subscribe(or -> or.ifPresent( r->log.info("selected : {}", r)));

        ResourceService rs = Application.CONTEXT.findBean(ResourceService.class).get();

        tableViewer.setInput(rs.getResources());

    }
}
