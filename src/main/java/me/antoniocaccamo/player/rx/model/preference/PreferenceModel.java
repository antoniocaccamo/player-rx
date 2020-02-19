package me.antoniocaccamo.player.rx.model.preference;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;

import java.util.List;
import java.util.UUID;

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

}
