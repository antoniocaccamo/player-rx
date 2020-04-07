package me.antoniocaccamo.player.rx.model.preference;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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

    private String weatherLatlng;

    @JsonProperty("player")
    private List<MonitorModel> monitors;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("uuid", uuid)
                .append("computer", computer)
                .append("size", size)
                .append("location", location)
                .append("sendAllEmail", sendAllEmail)
                .append("weatherLatlng", weatherLatlng)
                .append("monitors", monitors)
                .toString();
    }
}
