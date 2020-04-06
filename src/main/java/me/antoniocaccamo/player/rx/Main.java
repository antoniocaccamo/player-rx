package me.antoniocaccamo.player.rx;

import com.diffplug.common.base.DurianPlugins;
import com.diffplug.common.rx.RxTracingPolicy;
import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.server.EmbeddedServer;
import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.player.rx.ui.MainUI;

@Slf4j
public class Main {

    public static ApplicationContext CONTEXT ;

    public static void main(String[] args) {

        DurianPlugins.register(RxTracingPolicy.class, new RxTracingPolicy.LogSubscriptionTrace());

        try (ApplicationContext context = ApplicationContext.run()) {

            CONTEXT = context;

            CONTEXT.findBean(EmbeddedServer.class)
                    .ifPresent( srv -> srv.start());

            CONTEXT.findBean(MainUI.class).get();

            CONTEXT.findBean(EmbeddedServer.class)
                    .ifPresent( srv -> srv.stop());

        } catch (Exception e) {
            log.error("error occurred", e);
        } finally {
            log.info("closing");
           // System.exit(0);
        }
    }
}