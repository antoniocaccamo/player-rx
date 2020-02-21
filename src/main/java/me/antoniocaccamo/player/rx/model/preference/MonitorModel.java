package me.antoniocaccamo.player.rx.model.preference;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author antoniocaccamo on 07/02/2020
 */
@Slf4j
@Getter @Setter
public class MonitorModel extends Model {

    private SizeModel size;

    private LocationModel location;

    private String name;

    private String sequence;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("size", size)
                .append("location", location)
                .append("name", name)
                .append("sequence", sequence)
                .toString();
    }
}
