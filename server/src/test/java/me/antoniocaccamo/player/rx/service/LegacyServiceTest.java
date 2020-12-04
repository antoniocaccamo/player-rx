package me.antoniocaccamo.player.rx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.micronaut.test.annotation.MicronautTest;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.model.resource.RemoteResource;
import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author antoniocaccamo on 09/04/2020
 */

@MicronautTest @Slf4j
class LegacyServiceTest {

    @Inject
    private LegacyService legacyService;

    @Test
    void readSequence() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        File  file = Paths.get("test.xseq").toFile();


        assertTrue(file.exists(), String.format("file not found : %s", file.getAbsoluteFile()));

        Optional<Sequence> sequence =
                legacyService.readLeagacySequence(file.getAbsolutePath())
        ;
        assertNotNull(sequence.get());
        Sequence sq = sequence.get();

        sq.getMedias().add(
                Media.builder()
                        .duration(Duration.ofSeconds(5))
                        .resource(RemoteResource.builder()
                                .withType(Constants.Resource.Type.PHOTO)
                                .withRemote(Constants.Resource.Remote.FTP)
                                .build()
                        )
                        .build()
        );

        String serialization = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sq);

       log.info("sequence serialization: {}" , serialization );

       log.info("from serialization: {}" , mapper.readValue(serialization, Sequence.class) );

    }
}