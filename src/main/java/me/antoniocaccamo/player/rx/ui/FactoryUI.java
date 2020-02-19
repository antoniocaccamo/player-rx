package me.antoniocaccamo.player.rx.ui;

import io.micronaut.context.annotation.Factory;
import me.antoniocaccamo.player.rx.model.resource.AbstractResource;
import org.eclipse.swt.widgets.Composite;


/**
 * @author antoniocaccamo on 19/02/2020
 */
@Factory
public class FactoryUI {

    public AbstractUI createViewer(Composite parent, AbstractResource.TYPE type ) {

        AbstractUI viewer = null;

        switch (type) {

            case PHOTO:
                viewer = new PhotoUI(parent);
                break;


            default:
                viewer = new BlackUI(parent);
        }

        return viewer;
    }
}
