package me.antoniocaccamo.player.rx.ui.library;

import java.util.concurrent.CountDownLatch;

import com.diffplug.common.rx.RxBox;
import com.diffplug.common.swt.Layouts;

import com.diffplug.common.swt.widgets.FlatBtn;
import me.antoniocaccamo.player.rx.ApplicationUI;
import me.antoniocaccamo.player.rx.event.resource.ResourceEvent;
import me.antoniocaccamo.player.rx.event.resource.SelecteResourceEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.Application;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.service.SequenceService;
import me.antoniocaccamo.player.rx.ui.monitor.MonitorBrowserUI;

/**
 * @author antoniocaccamo on 05/05/2020
 */
@Slf4j
public class PreviewLibraryUI extends Composite {

    public PreviewLibraryUI(Composite parent/*, PublishSubject<ResourceEvent> resourcePublishSubject*/) {
        super(parent, SWT.NONE);

        Layouts.setGrid(this).numColumns(1).columnsEqualWidth(false).margin(0).spacing(0);

        SequenceService sequenceService = Application.CONTEXT.getBean(SequenceService.class);

        Group group = new Group(this, SWT.NONE);
        Layouts.setGrid(group).numColumns(1).spacing(0).margin(0);
        //Layouts.setGrid(group);
        //Layouts.setGridData(group).grabAll();
        group.setText("preview");
        Layouts.setGrid(group).numColumns(1);
        Layouts.setGridData(group).grabAll();
        final CountDownLatch latch = new CountDownLatch(1);
        MonitorBrowserUI composite = new MonitorBrowserUI(null, group, latch);
        Layouts.setGridData(composite).grabAll();

        Composite buttoComposite = new Composite(group, SWT.SHADOW_ETCHED_OUT | SWT.CENTER);
        Layouts.setGrid(buttoComposite).numColumns(3);
        Layouts.setGridData(buttoComposite).grabHorizontal().horizontalAlignment(SWT.CENTER);

        RxBox<Boolean> visibleBox = RxBox.of(Boolean.FALSE);

        FlatBtn button = new FlatBtn(buttoComposite, SWT.PUSH );
        button.setText("play");

        ApplicationUI.RESOURCE_EVENT_BUS
                .filter(resourceEvent -> resourceEvent instanceof SelecteResourceEvent)
                .map(resourceEvent -> (SelecteResourceEvent) resourceEvent)
                .subscribe( sre -> {
                    Resource resource = sre.getResource();
                    composite.setCurrent(
                            Media.builder()
                                    .resource(resource)
                                    .resourceHash(resource.getHash())
                                    .duration( resource.getDuration() != null ? resource.getDuration() : java.time.Duration.ofSeconds(5))
                                    .build()
                    );
                    visibleBox.set(resource.isVideo());

                }
        );

        visibleBox.asObservable().subscribe(b -> button.setEnabled(b));

        visibleBox.set(Boolean.FALSE);
    }
}