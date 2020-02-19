package me.antoniocaccamo.player.rx.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

}
