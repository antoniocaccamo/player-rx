package me.antoniocaccamo.player.rx.model.sequence;

import lombok.Data;
import me.antoniocaccamo.player.rx.model.Model;

import java.time.Duration;

@Data
public class Media implements Cloneable{

    private Model.Location location;
    private Model.Type         type;
    private String             path;
    private Duration       duration;
}
