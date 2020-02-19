package me.antoniocaccamo.player.rx.service.impl;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.model.resource.AbstractResource;
import me.antoniocaccamo.player.rx.service.TranscodeService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author antoniocaccamo on 19/02/2020
 */
@Singleton
@Slf4j
public class HLSTrancodeServiceImpl implements TranscodeService {

    private BlockingQueue<AbstractResource> trancodingQueue;

    private ExecutorService executorService;

    private int transcoders = 5;

    @PostConstruct
    public void postConstruct(){
        trancodingQueue = new LinkedBlockingQueue<>();
        executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < transcoders; i++  ) {
            executorService.submit( new TrancoderTask(trancodingQueue));
        }
        log.info("{}} service started", getClass().getSimpleName());
    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
        executorService.shutdownNow();
    }

    @Override
    public void transcode(AbstractResource resource) {
        if ( AbstractResource.TYPE.VIDEO.equals(resource.getType())){
            log.info("video resource to trancode : {}", resource);
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
            while (true){
                AbstractResource resource = null;
                try {
                   resource = trancodingQueue.take();
                } catch (InterruptedException e) {
                    log.info("{} interrupted" , Thread.currentThread().getName());
                }
            }
        }
    }
}
