package me.antoniocaccamo.player.rx.model.sequence;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.preference.LoadedSequence;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SequenceLooper {


    private final Lock lock = new ReentrantLock();
    private  int _loop    = 0;
    private  int _current = 0;
    private  int _next    = -1;


    @Getter
    private LoadedSequence loadedSequence;


    public Optional<Media> next()   {
        return next(LocalDateTime.now());
    }


    public void setLoadedSequence(LoadedSequence loadedSequence){
        this.loadedSequence = loadedSequence;
        reset();
        log.info("sequence to loop : {}", loadedSequence.getName() );
    }

    private void reset() {
        _loop    =  0;
        _current =  0;
        _next    = -1;
    }

    private Optional<Media> next(final LocalDateTime now) {
        AtomicReference<Media> media = new AtomicReference<>();
        if (lock.tryLock() ) {
            try {
                if ( loadedSequence.hasSequence()) {
                    boolean found = false;
                    while ( ! found ) {
                        _next = ++ _next % loadedSequence.getSequence().getMedias().size();
                        media.set(loadedSequence.getSequence().getMedias().get(_next));
                        if (media.get().isPlayable(now)) {
                            found = true;
                        } else {
                            log.warn("@@ -- TODO --");
                            found = true;
                        }
                        _loop += _next == 0 ? 1 : 0;
                    }
                    _loop += _next == 0 ? 1 : 0;
                }
            } finally {
                lock.unlock();
            }
        }
        return Optional.ofNullable(media.get());
    }

}
