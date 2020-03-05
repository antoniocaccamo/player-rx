package me.antoniocaccamo.player.rx.model.sequence;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SequenceLooper {

    private int _loop;

    private  Media head;
    private  Media tail;

    private Media current;

    private Media playing;

    private ReadWriteLock MY_LOCK = new ReentrantReadWriteLock();

    private LinkedList<Media> list  ;


    public SequenceLooper() {
        this( new LinkedList());
    }

    public SequenceLooper(List<Media> medias){
        prepareLoopfor(medias);
    }

    public void prepareLoopfor(List<Media> medias){
        list.clear();
        list.addAll(medias);
        head = list.peekFirst();
        tail = list.peekLast();
        current = head;
        playing = null;
    }

    public Media getNextMedia()   {
        return getNextMedia(LocalDateTime.now());
    }

    private Media getNextMedia(LocalDateTime now) {
        return null;
    }

//    private Media next(final LocalDateTime now) {
//        MY_LOCK.readLock().lock();
//        Media toPlay = null;
//        LocalDateTime saved = LocalDateTime.from(now);
//        try {
//            Media nextMedia2Play = null;
//            Media v = (Media) getVideos().getVideo(this.nextIndex);
//            // is the next playable ?
//            logger.debug("now is : {} ", now);
//            if (v.isPlayable(now)) {
//                nextMedia2Play = v;
//                this.nextIndex++;
//                checkEnd();
//            } else {
//                this.nextIndex++;
//                checkEnd();
//                Media vv = null;
//                boolean nextFound = false;
//                while (!nextFound && v != vv) {
//                    vv = (Media) getVideos().getVideo(this.nextIndex);
//                    this.nextIndex++;
//                    checkEnd();
//                    logger.debug("saved is : {}  ", saved);
//                    if (vv.isPlayable(saved)) {
//                        nextFound = true;
//                    }
//                }
//                if (nextFound) {
//                    nextMedia2Play = vv;
//                }
//            }
//            return nextMedia2Play;
//        } finally {
//            MY_LOCK.unlock();
//        }
//    }
//
//    private Media getNextMedia(final LocalDateTime now) {
//            MY_LOCK.readLock();
//            Media next = null;
//            LocalDateTime saved = LocalDateTime.from(now);
//            boolean found = false;
//
//            try {
//
//                if ( ! mediaQueue.isEmpty() ) {
//
//                    if ( mediaQueue.getTotal() == 1 ) {
//                        Media media = (Media) mediaQueue.getFirst().element;
//                        if ( media.isPlayable(now)) {
//                            mediaQueue.setPlaying(mediaQueue.getFirst());
//                            next = media;
//                        }
//                        return next;
//                    }
//
//                    if ( mediaQueue.getPlaying() == null  ) {
//                        mediaQueue.setPlaying(mediaQueue.getFirst());
//                        Media media = (Media) mediaQueue.getPlaying().element;
//                        if ( media.isPlayable(now)) {
//                            mediaQueue.setPlaying(mediaQueue.getFirst());
//                            return media;
//                        }
//                    }
//
//                    Node tmp = mediaQueue.getPlaying();
//                    tmp = tmp.next;
//                    while ( ! found && tmp != mediaQueue.getPlaying() ){
//                        Media media = (Media) tmp.element;
//                        if ( media.isPlayable(now)) {
//                            mediaQueue.setPlaying(tmp);
//                            return media;
//                        }
//                        tmp = tmp.next;
//                        if (tmp == mediaQueue.getLast()) {
//                            cycle++;
//                        }
//                    }
//                }
//
//            } finally {
//                MY_LOCK.readLock().unlock();
//                return next;
//            }
//        }
//    }
}
