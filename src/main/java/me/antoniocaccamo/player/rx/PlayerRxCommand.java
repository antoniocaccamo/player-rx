package me.antoniocaccamo.player.rx;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "player-rx", description = "...",
        mixinStandardHelpOptions = true)
@Slf4j
public class PlayerRxCommand implements Runnable {

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(PlayerRxCommand.class, args);
    }

    public void run() {
        // business logic here
        if (verbose) {
            log.info("Hi!");
        }

        
    }
}
