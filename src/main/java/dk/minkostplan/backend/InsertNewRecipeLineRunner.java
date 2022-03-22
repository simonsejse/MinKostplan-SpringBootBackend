package dk.minkostplan.backend;

import org.apache.xmlbeans.impl.tool.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InsertNewRecipeLineRunner implements CommandLineRunner {

    private final Logger log = LoggerFactory.getLogger(InsertNewRecipeLineRunner.class);

    @Override
    public void run(String... args) throws Exception {
        log.info("Creating a new random recipe");
    }
}
