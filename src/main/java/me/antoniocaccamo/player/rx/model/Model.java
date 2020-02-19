package me.antoniocaccamo.player.rx.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.EnumSet;

/**
 * @author antoniocaccamo on 07/02/2020
 */
public abstract class Model {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    public enum Location {
        LOCAL,
        REMOTE
    }

    public enum Type {
        VIDEO,
        PHOTO
    }
}
