package me.antoniocaccamo.player.rx.service.impl;

import me.antoniocaccamo.player.rx.model.sequence.Media;
import me.antoniocaccamo.player.rx.repository.MediaRepository;
import me.antoniocaccamo.player.rx.service.MediaService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author antoniocaccamo on 22/04/2020
 */
@Singleton
public class MediaServiceImpl implements MediaService {

    @Inject
    private MediaRepository mediaRepository;


    @Override
    public void save(Media media) {
        mediaRepository.save(media);
    }
}
