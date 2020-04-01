package me.antoniocaccamo.player.rx.service.impl;

import com.google.common.hash.Hashing;
import io.micronaut.context.annotation.Value;
import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.resource.RemoteResource;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import me.antoniocaccamo.player.rx.repository.ResourceRepository;
import me.antoniocaccamo.player.rx.service.ResourceService;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton @Slf4j
public class ResourceServiceImpl implements ResourceService {

    @Value("${micronaut.application.res-library-file}")
    private String resLibraryFile;

    @Inject
    private ResourceRepository resourceRepository;

    private Map<String, Resource> resourceMap;

    @PostConstruct
    public void postConstruct() throws FileNotFoundException {

        /*
        Path path = Paths.get(resLibraryFile);
        log.info("loading resource library : file {} exists ? : {}" , path.toAbsolutePath(), path.toFile().exists());
        // @TODO loading resource library
        if ( path.toFile().exists() ) {
            log.warn("load resource library...");

          //  Iterable<Object> rss = new Yaml().loadAll( new FileInputStream(path.toFile()));
            // rss.forEach( rs ->          log.warn("rs : {}", rs));
        }
        */




        resourceMap = Observable.fromIterable(resourceRepository.findAll()).toMap( x-> Hashing.sha512().hashString(x.toString(), StandardCharsets.UTF_8).toString()).blockingGet();


    }

    public Map getResourceMap() {
       return resourceMap;
    }

    @Override
    public Iterable<Resource> getResources() {
        return resourceRepository.findAll();
    }

    //@PreDestroy
   //public void preDestroy() {
   //    log.info("{} service destroying", getClass().getSimpleName());

   //    log.info("saving resources...");
   //    try ( FileWriter fw = new FileWriter(new File(resLibraryFile)) ) {
   //        new Yaml().dump(getResourceMap(), fw);
   //    } catch (Exception e) {
   //        log.error("error occurred", e);
   //    }
   //}
}
