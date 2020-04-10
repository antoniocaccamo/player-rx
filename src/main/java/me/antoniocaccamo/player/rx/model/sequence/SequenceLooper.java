package me.antoniocaccamo.player.rx.model.sequence;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SequenceLooper {

    @JsonIgnore
    @Builder.Default @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) @Transient
    private final Lock MY_LOCK = new ReentrantLock();
    @JsonIgnore @Builder.Default @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) @Transient
    private  int _loop    = 0;
    @JsonIgnore @Builder.Default @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) @Transient
    private  int _current = 0;
    @JsonIgnore @Builder.Default @Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE) @Transient
    private  int _next    = -1;

    @Getter @Setter
    private Optional<Sequence> optionalSequence = Optional.empty();



    public Optional<Media> next()   {
        return next(LocalDateTime.now());
    }

    private Optional<Media> next(final LocalDateTime now) {

        AtomicReference<Media> media = new AtomicReference<>();

        optionalSequence.ifPresent( sq ->{

            MY_LOCK.lock();
            boolean found = false;
            try {
                while ( ! found ) {
                    _next = ++ _next % sq.getMedias().size();
                    media.set(sq.getMedias().get(_next));
                    if (media.get().isPlayable(now)) {
                        found = true;
                    } else {
                        log.warn("@@ -- TODO --");
                        found = true;
                    }
                    _loop += _next == 0 ? 1 : 0;
                }
            } finally {
                MY_LOCK.unlock();
            }
        });

        return Optional.ofNullable(media.get());

    }

/*
    public SequenceLooper() {
        this( new LinkedList());
    }

    public Media next()   {
        return next(LocalDateTime.now());
    }

    private Media next(final LocalDateTime now) {
        MY_LOCK.lock();
        LocalDateTime saved = LocalDateTime.now();
        try {
            Media nextMedia2Play = null;
            Media v = (Media) getVideos().getVideo(this.nextIndex);
            // is the next playable ?
            logger.debug("now is : {} ", now);
            if (v.isPlayable(now)) {
                nextMedia2Play = v;
                this.nextIndex++;
                checkEnd();
            } else {
                this.nextIndex++;
                checkEnd();
                Media vv = null;
                boolean nextFound = false;
                while (!nextFound && v != vv) {
                    vv = (Media) getVideos().getVideo(this.nextIndex);
                    this.nextIndex++;
                    checkEnd();
                    logger.debug("saved is : {}  ", saved);
                    if (vv.isPlayable(saved)) {
                        nextFound = true;
                    }
                }
                if (nextFound) {
                    nextMedia2Play = vv;
                }
            }
            return nextMedia2Play;
        } finally {
            MY_LOCK.unlock();
        }
    }

 */
}
