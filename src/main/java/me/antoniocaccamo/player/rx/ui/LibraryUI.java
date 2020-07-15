package me.antoniocaccamo.player.rx.ui;

import com.diffplug.common.swt.Layouts;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.resource.Resource;
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

    private final PublishSubject<Resource> resourcePublishSubject;

    public LibraryUI(Composite parent) {
        super(parent, SWT.NONE);

        resourcePublishSubject = PublishSubject.create();

        Layouts.setGrid(this).numColumns(1).columnsEqualWidth(false).margin(0).spacing(0);

        SashForm parentSashForm = new SashForm(this, SWT.HORIZONTAL);
        Layouts.setGridData(parentSashForm).grabAll();

        SashForm sashForm = new SashForm(parentSashForm, SWT.VERTICAL);
        //Layouts.setGridData(sashForm).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();


        new SequenceLibraryUI(sashForm, resourcePublishSubject);
        new ResourceLibraryUI(sashForm, resourcePublishSubject);


        sashForm = new SashForm(parentSashForm, SWT.VERTICAL);
        //Layouts.setGridData(sashForm).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();

        //Layouts.setGridData(new ResourceLibraryUI(sashForm)).grabAll();

        new PreviewLibraryUI(sashForm, resourcePublishSubject);

        int [] weights = {3,1};
        parentSashForm.setWeights(weights);

    }
}
