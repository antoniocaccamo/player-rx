package me.antoniocaccamo.player.rx.helper;

import io.reactivex.Observable;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.service.ResourceService;
import me.antoniocaccamo.player.rx.service.SequenceService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author antoniocaccamo on 10/03/2020
 */
@Singleton
@Slf4j
public class InitHelper {

    //@Autowired
    //private MediaService mediaService;

    @Inject
    private SequenceService sequenceService;

    @Inject
    private ResourceService resourceService;

 
    public void getDefaultSquence() {

        if ( log.isDebugEnabled() ) {
            Observable.fromIterable(resourceService.getResources())
                    .subscribe(resource -> log.debug("init : resources {}", resource));

            Observable.fromIterable(sequenceService.getLoadedSequences())
                    .subscribe(loadedSequence ->  log.debug("init : loadedSequence {} -> {}", loadedSequence.getName(), loadedSequence.getPath()));
        }
    }

/*
    public Sequence  getDefaultSquence(){

        Sequence sequence = null;

        sequence = sequenceService.getSequenceByName(Constants.Sequence.DefaultSequenceName)
                .orElseGet(() -> Constants.Sequence.DEFAULT_SEQUENCE )
        ;

        return sequence;
    }

    
    @Transactional
    private Sequence createDefaultSequence() {

        log.warn("creating default sequence ..");

        Sequence sequence = null;
        try {
        sequence = Constants.Sequence.DEFAULT_SEQUENCE;
        sequence.getMedias()
                .stream()
                .forEach(media -> {
                    Resource resource = media.getResource();
                    resourceService.save(resource);
                    //media.setResource(resource);
                    mediaService.save(media);
                });
        sequenceService.save(sequence, Files.createTempFile("xxx", ".syml"));
        ObjectMapper mapper = new ObjectMapper();
        log.info("default sequence saved : {} " , sequence);
		} catch (Exception e) {
            log.error("error occurred", e);
        }

        return sequence;

    }

*/

}
