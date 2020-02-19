package me.antoniocaccamo.player.rx.service.impl;

import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.preference.PreferenceModel;
import me.antoniocaccamo.player.rx.service.PreferenceService;
import me.antoniocaccamo.player.rx.service.ResourceService;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author antoniocaccamo on 07/02/2020
 */
@Singleton @Slf4j
public class PreferenceServiceImpl implements PreferenceService {

    @Inject
    private ResourceService resourceService;

    @Value("${micronaut.application.pref-file}") @NotNull
    private String prefFile;

    private PreferenceModel mainViewModel;

    @PostConstruct
    public void postConstruct() throws FileNotFoundException {

        File f = new File(prefFile);
        log.info("reading file : {}", f.getAbsolutePath());

        mainViewModel = new Yaml(new Constructor(PreferenceModel.class)).load(new FileInputStream(f));
    }


    @Override
    public PreferenceModel read() {
        log.info("mainViewModel : {}", mainViewModel);
        return mainViewModel;
    }

    @Override
    public void save() {
        throw new RuntimeException("not yet implemented");
    }
}
