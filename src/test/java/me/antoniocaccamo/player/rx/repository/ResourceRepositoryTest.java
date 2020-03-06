package me.antoniocaccamo.player.rx.repository;

import io.micronaut.test.annotation.MicronautTest;
//import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.service.ResourceService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

@MicronautTest
//@Slf4j
public class ResourceRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(ResourceRepositoryTest.class);


    @Inject
    private ResourceRepository resourceRepository;

    @Inject
    private ResourceService resourceService;


    @Test
    public void saveAndResource(){

        resourceService.getResources().stream()
                .forEach( resource ->
                    logger.info("saved : {}", resourceRepository.save(resource))
                );

        resourceRepository.findAll()
                .forEach(resource -> logger.info("read  : {}", resource));



    }
}
