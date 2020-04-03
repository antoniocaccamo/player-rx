package me.antoniocaccamo.player.rx.model.preference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.beans.Transient;
import java.time.LocalTime;

/**
 * @author antoniocaccamo on 07/02/2020
 */
@Slf4j
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MonitorModel extends Model {

    private SizeModel size;

    private LocationModel location;

    private String name;

    private String sequence;

    public LocalTime getFrom() {
        return LocalTime.of(9,0);
    }

    public LocalTime getTo(){
        return LocalTime.of(20,0);
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("size", size)
                .append("location", location)
                .append("name", name)
                .append("sequence", sequence)
                .toString();
    }

    public boolean isTimed(){
        return false;
    }
}
