package me.antoniocaccamo.player.rx.model.preference;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.helper.SWTHelper;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.Point;

/**
 * @author antoniocaccamo on 07/02/2020
 */
@Slf4j
@Getter @Setter
public class SizeModel extends Model implements Point {

    private int width;

    private int height;

    @Override
    public org.eclipse.swt.graphics.Point toPoint() {
        return SWTHelper.toPoint(width, height);
    }
}
