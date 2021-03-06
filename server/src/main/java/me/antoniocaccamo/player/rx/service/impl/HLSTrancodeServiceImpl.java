package me.antoniocaccamo.player.rx.service.impl;

import com.diffplug.common.swt.SwtExec;
import io.micronaut.context.annotation.Value;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.function.FfmpgeHlsFunction;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.service.TranscodeService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Singleton;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author antoniocaccamo on 19/02/2020
 */
@Singleton
@Slf4j
public class HLSTrancodeServiceImpl implements TranscodeService {

    @Value("${micronaut.application.resource-prefix-path}")
    private String resourcePrefixPath;

    private final AtomicBoolean running = new AtomicBoolean(Boolean.FALSE);

    private BlockingQueue<Resource> toTrancodegQueue     = new LinkedBlockingQueue<>();
    private final BlockingQueue<String> trancodingQueue  = new LinkedBlockingQueue<>();

    private ExecutorService executorService;

    private int transcoders = 5;
    
    private PublishSubject<Resource> resourcePublishSubject = PublishSubject.create();
    private Disposable resourceDisposable;

    @PostConstruct
    public void postConstruct() {
        
        Function<Resource, Resource> ffmpgeHlsFunction = new FfmpgeHlsFunction(resourcePrefixPath);
        
        resourceDisposable = resourcePublishSubject
                .subscribeOn(Schedulers.computation())
                .map( ffmpgeHlsFunction )
                .observeOn(  SwtExec.async().getRxExecutor().scheduler())        
                .subscribe( 
                        resource -> log.info("transcoded : {}", resource), 
                        throwable-> log.error("error occurred", throwable)
                );
    }

    @PreDestroy
    public void preDestroy() {
        log.info("{} service destroying", getClass().getSimpleName());
           this.resourceDisposable.dispose();
    }

    @Override
    public void transcode(Resource resource) {

        if ( ! resource.needsTrancode())
            return;
        this.resourcePublishSubject.onNext(resource);
    }


//    private class TrancoderTask implements Runnable {
//
//        private BlockingQueue<Resource> toTrancodegQueue;
//        private final BlockingQueue<String> trancodingQueue;
//
//        private TrancoderTask(BlockingQueue<Resource> toTrancodegQueue, BlockingQueue<String> trancodingQueue) {
//            this.toTrancodegQueue = toTrancodegQueue;
//            this.trancodingQueue = trancodingQueue;
//        }
//
//        @Override
//        public void run() {
//            log.info("{} ready to trancode..", Thread.currentThread().getName());
//            Resource resource = null;
//            while ( running.get() ){
//                try {
//                    resource = toTrancodegQueue.poll(5, TimeUnit.SECONDS);
//                    if ( resource == null)
//                        continue;
//                    if (  ! trancodingQueue.contains(resource.getHash()) ) {
//                        trancodingQueue.offer(resource.getHash());
//                        log.info("{} : video resource to trancode : {}", Thread.currentThread().getName(), resource);
//
//                        //Thread.sleep(resource.getDuration().toMillis());
//
//                        File source = new File(resource.getPath());
//                        LocalDateTime sdt = LocalDateTime.ofInstant(Instant.ofEpochMilli(source.lastModified()), ZoneId.systemDefault());
//                        log.info("source : {} => exists : {} : last modified : {}",
//                                source.getAbsolutePath(),
//                                source.exists() ,
//                                source.exists() ? DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(sdt) : " - "
//                        );
//
//                        File target = new File(Constants.Resource.getVideoHLS(resourcePrefixPath, resource));
//                        LocalDateTime tdt = LocalDateTime.ofInstant(Instant.ofEpochMilli(target.lastModified()), ZoneId.systemDefault());
//                        log.info("target : {} => exists : {} : last modified : {}",
//                                target.getAbsolutePath(),
//                                target.exists() ,
//                                target.exists() ? DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(tdt) : " - "
//                        );
//
//                        if ( target.exists() ) {
//                            if ( tdt.isAfter(sdt)) {
//                                log.info("not necessary to transcode resource : {}", resource);
//                                continue;
//                            }
//                            File targetParent = target.getParentFile();
//                            Stream.of(targetParent.listFiles())
//                                    .forEach(file -> log.info("deleting {} => {}", file, file.delete()));
//                            log.info("deleting {} => {}",targetParent.getAbsolutePath(), targetParent.delete());
//                        }
//                        target.getParentFile().mkdirs();
//                        target.createNewFile();
//
//                        AudioAttributes audioAttr = new AudioAttributes();
//                        VideoAttributes videoAttr = new VideoAttributes();
//
//                        audioAttr.setChannels(2);
//                        audioAttr.setCodec("aac");
//                        audioAttr.setBitRate(128000);
//                        audioAttr.setSamplingRate(44100);
//
//                        videoAttr.setCodec("libx264");
//                        videoAttr.setBitRate(4000000);
//                        videoAttr.setX264Profile(VideoAttributes.X264_PROFILE.BASELINE);
//
//                        EncodingAttributes encodingAttr = new EncodingAttributes();
//                        encodingAttr.setFormat("hls");
//                        encodingAttr.setAudioAttributes(audioAttr);
//                        encodingAttr.setVideoAttributes(videoAttr);
//
//                        log.info("encoding attributes : {}", encodingAttr.toString());
//
//                        Encoder encoder = new Encoder();
//
//                        Resource finalResource = resource;
//                        encoder.encode(new MultimediaObject(source), target, encodingAttr, new EncoderProgressListener() {
//                            @Override
//                            public void sourceInfo(MultimediaInfo multimediaInfo) {
//                                log.info("multimediaInfo : {}", multimediaInfo);
//                                finalResource.setDuration(Duration.ofMillis( multimediaInfo.getDuration()));
//                                log.info("Duration.ofMillis( multimediaInfo.getDuration() : {}", finalResource.getDuration());
//                            }
//
//                            @Override
//                            public void progress(int i) {
//                                log.info("progress : {} / 1000", i);
//                            }
//
//                            @Override
//                            public void message(String s) {
//                                log.info("message : {}", s);
//                            }
//                        });
//
//                        log.info("{} :  trancoded : {}", Thread.currentThread().getName(), resource);
//                    }
//                } catch (IOException | IllegalArgumentException | EncoderException ee) {
//                    log.error("error occurred", ee);
//                    if ( resource != null)
//                        trancodingQueue.remove(resource.getHash());
//                }
//                catch (InterruptedException e) {
//                    log.error("{} interrupted" , Thread.currentThread().getName());
//                }
//            }
//        }
//    }
    
}
