package me.antoniocaccamo.player.rx.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.resource.AbstractResource;
import me.antoniocaccamo.player.rx.service.TranscodeService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author antoniocaccamo on 19/02/2020
 */
@Singleton
@Slf4j
public class HLSTrancodeServiceImpl implements TranscodeService {

    private final AtomicBoolean running = new AtomicBoolean(Boolean.FALSE);

    private BlockingQueue<AbstractResource> trancodingQueue;

    private ExecutorService executorService;

    private int transcoders = 5;

    @PostConstruct
    public void postConstruct(){
        trancodingQueue = new LinkedBlockingQueue<>();
        running.set(Boolean.TRUE);
        executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < transcoders; i++  ) {
            executorService.submit( new TrancoderTask(trancodingQueue));
        }
        log.info("{}} service started", getClass().getSimpleName());
    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
        running.set(Boolean.FALSE);
        executorService.shutdown();
        try {
            executorService.awaitTermination(2, TimeUnit.SECONDS);
            executorService.shutdownNow();

        } catch (InterruptedException ex) {
            log.error("error occurred", ex);
            executorService.shutdownNow();
        } finally {
            log.info("{} stopped" , getClass().getSimpleName());
        }
    }

    @Override
    public void transcode(AbstractResource resource) {
        if ( AbstractResource.TYPE.VIDEO.equals(resource.getType())){
            log.info("add video resource to trancode : {}", resource);
            trancodingQueue.offer(resource);
        }
    }


    private class TrancoderTask implements Runnable {

        private final BlockingQueue<AbstractResource> trancodingQueue;

        private TrancoderTask(BlockingQueue<AbstractResource> trancodingQueue) {
            this.trancodingQueue = trancodingQueue;
        }

        @Override
        public void run() {
            log.info("{} ready to trancode..", Thread.currentThread().getName());
            AbstractResource resource = null;
            while ( running.get() ){
                try {
                    resource = trancodingQueue.poll(5, TimeUnit.SECONDS);
                    if ( resource == null)
                        continue;
                    log.info("{} : video resource to trancode : {}", Thread.currentThread().getName(), resource);
                    Thread.sleep(resource.getDuration().toMillis());
                    log.info("{} :  trancoded : {}", Thread.currentThread().getName(), resource);
                } catch (InterruptedException e) {
                    log.info("{} interrupted" , Thread.currentThread().getName());
                }
            }
        }
    }
}
