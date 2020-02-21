package me.antoniocaccamo.player.rx.service.impl;

import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.service.ResourceService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

@Singleton @Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Value("${micronaut.application.res-library-file}")
    private String resLibraryFile;

    private ConcurrentHashMap<String, Object> resources;

    @PostConstruct
    public void postConstruct(){
        Path path = Paths.get(resLibraryFile);
        log.info("loading resource library : file {} exists ? : {}" , path.toAbsolutePath(), path.toFile().exists());
        // @TODO loading resource library
        if ( path.toFile().exists() ) {
            log.warn("load resource library...");
        }
    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
    }
}
