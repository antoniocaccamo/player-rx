package me.antoniocaccamo.player.rx.model.jackson;

import lombok.*;

import java.util.Collection;

/**
 * @author antoniocaccamo on 22/04/2020
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class CollectionWrapper<T> {

    private Collection<T> collection ;

    public void clear(){
        if ( collection != null )
            collection.clear();
    }
}
