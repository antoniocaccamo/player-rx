package me.antoniocaccamo.player.rx.helper;

import org.eclipse.swt.graphics.Point;

import javax.validation.constraints.NotNull;

/**
 * @author antoniocaccamo on 18/02/2020
 */
public class SWTHelper {

    public static Point toPoint(@NotNull int i1, @NotNull  int i2){
        return new Point(i1, i2);
    }
}
