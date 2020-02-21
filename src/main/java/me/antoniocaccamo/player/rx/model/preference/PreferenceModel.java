package me.antoniocaccamo.player.rx.model.preference;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;

import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author antoniocaccamo on 07/02/2020
 */
@Slf4j
@Getter @Setter
public class PreferenceModel extends Model {

    private String uuid = UUID.randomUUID().toString();

    private String computer;

    private SizeModel size;

    private LocationModel location;

    private String sendAllEmail;

    private List<MonitorModel> monitors;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("uuid", uuid)
                .append("computer", computer)
                .append("size", size)
                .append("location", location)
                .append("sendAllEmail", sendAllEmail)
                .append("monitors", monitors)
                .toString();
    }
}
