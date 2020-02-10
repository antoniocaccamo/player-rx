package me.antoniocaccamo.player.rx.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

/**
 * @author antoniocaccamo on 07/02/2020
 */
@Slf4j
@Getter @Setter
public class MainViewModel extends Model {

    private String uuid = UUID.randomUUID().toString();

    private String computer;

    private SizeModel size;

    private LocationModel location;

    private String sendAllEmail;

    private List<MonitorModel> monitors;

}
