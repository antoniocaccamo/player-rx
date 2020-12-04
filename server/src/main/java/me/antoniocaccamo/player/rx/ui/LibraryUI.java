package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.Layouts;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.event.resource.ResourceEvent;
import me.antoniocaccamo.player.rx.ui.library.PreviewLibraryUI;
import me.antoniocaccamo.player.rx.ui.library.ResourceLibraryUI;
import me.antoniocaccamo.player.rx.ui.library.SequenceLibraryUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;

/**
 * @author antoniocaccamo on 02/03/2020
 */
@Slf4j
public class LibraryUI extends Composite {

//  private final PublishSubject<ResourceEvent> resourcePublishSubject;

    public LibraryUI(Composite parent) {
        super(parent, SWT.NONE);

//      resourcePublishSubject = PublishSubject.create();

        Layouts.setGrid(this).numColumns(1).columnsEqualWidth(false).margin(0).spacing(0);

        SashForm parentSashForm = new SashForm(this, SWT.VERTICAL);
        Layouts.setGridData(parentSashForm).grabAll();


        //Layouts.setGridData(sashForm).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();


        new SequenceLibraryUI(parentSashForm/*, resourcePublishSubject*/);

        SashForm sashForm = new SashForm(parentSashForm, SWT.HORIZONTAL);

        new ResourceLibraryUI(sashForm/*, resourcePublishSubject*/);


//        sashForm = new SashForm(parentSashForm, SWT.VERTICAL);
        //Layouts.setGridData(sashForm).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();

        new PreviewLibraryUI(sashForm/*, resourcePublishSubject*/);

        int [] weights = {3,1};
        sashForm.setWeights(weights);

    }
}
