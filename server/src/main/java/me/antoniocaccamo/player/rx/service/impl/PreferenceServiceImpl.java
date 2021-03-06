package me.antoniocaccamo.player.rx.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.micronaut.context.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.preference.Preference;
import me.antoniocaccamo.player.rx.service.LegacyService;
import me.antoniocaccamo.player.rx.service.PreferenceService;
import me.antoniocaccamo.player.rx.service.ResourceService;
import me.antoniocaccamo.player.rx.service.SequenceService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

/**
 * @author antoniocaccamo on 07/02/2020
 */
@Singleton
@Slf4j
public class PreferenceServiceImpl implements PreferenceService {

    @Inject
    private ResourceService resourceService;

    @Inject
    private LegacyService legacyPreferenceService;

    @Inject
    private SequenceService sequenceService;

    @Value("${micronaut.application.pref-file}") @NotNull
    private File prefFile;

    private Preference preferenceModel;

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    @PostConstruct
    public void postConstruct() throws IOException {
        log.info("preference file : {} exists {}", prefFile.getAbsolutePath(), prefFile.exists());
        if ( ! prefFile.exists() ) {
            log.warn(" ==>> reading from legacy <<===");
            preferenceModel = legacyPreferenceService.loadLegacyPreference();
            save();
        } else {
            preferenceModel = mapper.readValue(prefFile, Preference.class);
        }
        preferenceModel.getLoadedSequences().stream()
                .forEach(loadedSequence -> sequenceService.loadFromSource(loadedSequence));
    }

    @Override
    public Preference read() {
        if (log.isDebugEnabled())
            log.debug("preferenceModel : {}", preferenceModel);
        return preferenceModel;
    }

    @Override @PreDestroy
    public void save() throws IOException {
        log.info("saving preferences to file : {}", prefFile.getAbsolutePath());
        mapper.writerWithDefaultPrettyPrinter().writeValue(prefFile, preferenceModel);
    }

}
