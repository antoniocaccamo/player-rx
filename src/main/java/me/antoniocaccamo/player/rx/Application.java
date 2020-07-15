package me.antoniocaccamo.player.rx;

import com.diffplug.common.base.DurianPlugins;
import com.diffplug.common.rx.RxTracingPolicy;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;
import lombok.extern.slf4j.Slf4j;

import io.micronaut.context.event.ApplicationEventPublisher;
import me.antoniocaccamo.player.rx.event.application.StartApplicationEvent;

@Slf4j
public class Application {

    public static int SERVER_PORT = 8080;

    public static ApplicationContext CONTEXT ;

    public static int port;

    public static void main(String[] args) {

        DurianPlugins.register(RxTracingPolicy.class, new RxTracingPolicy.LogSubscriptionTrace());

//        try (ApplicationContext context = ApplicationContext.run()) {

//            CONTEXT = context;

//            CONTEXT.findBean(EmbeddedServer.class)
//                    .ifPresent( srv -> {
//                        log.info("starting server  :  ", srv.getURI());
//                        srv.start();
//                    });

//            CONTEXT.findBean(MainUI.class).get();

//            CONTEXT.findBean(EmbeddedServer.class)
//                    .ifPresent( srv -> {
//                        log.info("starting server  : {} ", srv);
//                        srv.stop();
//                    });

//        } catch (Exception e) {
//            log.error("error occurred", e);
//        } finally {
//            log.info("closing");
           // System.exit(0);
  //      }
//        try ( ApplicationContext context  = Micronaut.run(Main.class, args) ){
//            CONTEXT= context;
//            // CONTEXT.findBean(MainUI.class).get();
//            context.start();
//        }

        CONTEXT = Micronaut.run(Application.class, args);

        log.warn("###################################");
        CONTEXT.getBean(ApplicationEventPublisher.class).publishEvent( new StartApplicationEvent());

    }
}