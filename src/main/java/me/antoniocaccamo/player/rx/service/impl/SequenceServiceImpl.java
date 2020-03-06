package me.antoniocaccamo.player.rx.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.Model;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.model.resource.RemoteResource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import me.antoniocaccamo.player.rx.service.SequenceService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;

@Singleton @Slf4j
public class SequenceServiceImpl implements SequenceService {

    private final ObjectMapper mapper = new ObjectMapper();
    private Sequence dummy;


    @PostConstruct
    public void postConstruct(){
        dummy = Sequence.builder()
                .name("test sequence")
                .location(Model.Location.LOCAL)
                .medias(Arrays.asList(
                        Media.builder()
                                .resource(
                                        LocalResource.builder()
                                                .withType(Resource.TYPE.PHOTO)
//                                                .withLocation(Resource.LOCATION.LOCAL)
                                                .withPath("C:\\Windows\\Web\\Screen\\img100.jpg")
                                                .withDuration(Duration.ofSeconds(5))
                                                .build()
                                )
                                .build(),
                        Media.builder()
                                .resource(
                                        LocalResource.builder()
                                                .withType(Resource.TYPE.PHOTO)
  //                                              .withLocation(Resource.LOCATION.LOCAL)
                                                .withPath("C:\\Windows\\Web\\Screen\\img160.jpg")
                                                .withDuration(Duration.ofSeconds(15))
                                                .build()
                                )
                                .build(),
                        Media.builder()
                                .resource(
                                        RemoteResource.builder()
                                            .withType(Resource.TYPE.WEATHER)
                                            .build()
                                )
                                .build(),
                        Media.builder()
                                .resource(
                                        LocalResource.builder()
                                                .withType(Resource.TYPE.VIDEO)
//                                                .withLocation(Resource.LOCATION.LOCAL)
                                                .withPath("C:\\Users\\antonio\\Videos\\big_buck_bunny.mp4")
                                                .build()
                                )
                                .build()
                ))
                .build();
    }

    @Override
    public Sequence read( Path path){
        return read(Model.Location.LOCAL, path);
    }


    @Override
    public Sequence read(Model.Location location, Path path) {

        log.info("reading sequence from location {} and  path : {}", location, path);

        dummy.clone();
        dummy.setName(path.toFile().getName());

        try {
            log.info("dummy : {}", mapper.writeValueAsString(dummy) );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return dummy
        ;
    }

    @Override
    public void save(Sequence sequence, Path path) {
        try {
            log.info("sequence : {}", mapper.writeValueAsString(sequence) );
            mapper.writeValue(new FileWriter(sequence.getName()), sequence);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
        save(dummy, Paths.get("."));
    }
}
