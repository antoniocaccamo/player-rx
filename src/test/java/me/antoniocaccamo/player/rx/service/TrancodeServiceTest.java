package me.antoniocaccamo.player.rx.service;

import io.micronaut.test.annotation.MicronautTest;
import io.reactivex.Observable;
import me.antoniocaccamo.player.rx.config.Constants;
import me.antoniocaccamo.player.rx.model.resource.Resource;
import me.antoniocaccamo.player.rx.model.resource.LocalResource;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Duration;

@MicronautTest
class TrancodeServiceTest  {

    @Inject
    TranscodeService transcodeService;

    @Test
    public void testHelloWorldResponse() throws InterruptedException {

        Observable.just(1,2,3,4,5,6,7,8,9,10)
                .subscribe( i ->
                        transcodeService.transcode(
                                LocalResource.builder()
                                        .withDuration(Duration.ofSeconds(i))
                                        .withType(Constants.Resource.Type.VIDEO)
                                        .withPath("XXXX")
                                        .build()
                        ));
        Thread.sleep(20000);
    }
}
