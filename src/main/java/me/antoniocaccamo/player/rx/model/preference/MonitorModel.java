package me.antoniocaccamo.player.rx.model.preference;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants;
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

    private LocalTime from;

    private LocalTime to;

    private Constants.TimingEnum timing;



    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("size", size)
                .append("location", location)
                .append("name", name)
                .append("sequence", sequence)
                .append("timing", timing)
                .append("from", from)
                .append("to", to)

                .toString();
    }

    @JsonIgnore
    public boolean isTimed(){
        return  Constants.TimingEnum.TIMED.equals(timing) && ( from != null &&  to != null );
    }
}
