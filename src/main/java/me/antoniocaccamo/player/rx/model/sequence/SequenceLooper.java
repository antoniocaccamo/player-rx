package me.antoniocaccamo.player.rx.model.sequence;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class SequenceLooper {

    private int _loop;

    private  Media head;
    private  Media tail;

    private Media current;

    private Media playing;

    private Lock MY_LOCK = new ReentrantLock();

    private LinkedList<Media> list  ;

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
