package me.antoniocaccamo.player.rx.service.impl;

import com.google.common.hash.Hashing;
import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.resource.AbstractResource;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.service.ResourceService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton @Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Value("${micronaut.application.res-library-file}")
    private String resLibraryFile;

    private Map<String, AbstractResource> resources;

    @PostConstruct
    public void postConstruct(){
        Path path = Paths.get(resLibraryFile);
        log.info("loading resource library : file {} exists ? : {}" , path.toAbsolutePath(), path.toFile().exists());
        // @TODO loading resource library
        if ( path.toFile().exists() ) {
            log.warn("load resource library...");

        }
        resources = Arrays.asList(
                LocalResource.builder()
                        .withType(AbstractResource.TYPE.PHOTO)
                        .withLocation(AbstractResource.LOCATION.LOCAL)
                        .withPath("C:\\Windows\\Web\\Screen\\img100.jpg")
                        .withDuration(Duration.ofSeconds(5))
                        .build(),
                LocalResource.builder()
                        .withType(AbstractResource.TYPE.PHOTO)
                        .withLocation(AbstractResource.LOCATION.LOCAL)
                        .withPath("C:\\Users\\antonio\\Videos\\big_buck_bunny.mp4")
                        .build()
        )   .stream()
            .collect(
                    Collectors.toMap(x-> Hashing.sha512().hashString(x.toString(), StandardCharsets.UTF_8).toString(), x->x)
            );
    }

    public List<AbstractResource> getResources() {
        return resources.values().stream().collect(Collectors.toList());
    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
    }
}
