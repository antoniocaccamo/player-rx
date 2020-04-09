package me.antoniocaccamo.player.rx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.annotation.MicronautTest;
import me.antoniocaccamo.player.rx.model.sequence.Sequence;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author antoniocaccamo on 09/04/2020
 */

@MicronautTest
class LegacyServiceTest {

    @Inject
    private LegacyService legacyService;

    @Test
    void readSequence() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Optional<Sequence> sequence =
                legacyService.readSequence("D:\\development\\workspaces\\antoniocaccamo\\player-rx\\src\\test\\resources\\test.xseq")
        ;
        assertNotNull(sequence.get());
        System.out.println(String.format("sequence : %s" , sequence.get()));
    }
}