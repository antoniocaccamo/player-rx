package me.antoniocaccamo.player.rx.model;


/**
 * @author antoniocaccamo on 07/02/2020
 */
public abstract class Model {

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Model{");
        sb.append('}');
        return sb.toString();
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
