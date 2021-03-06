package dk.minkostplan.backend.linerunners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class InsertNewRecipeLineRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(InsertNewRecipeLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("Creating a new random recipe");

    }
}
