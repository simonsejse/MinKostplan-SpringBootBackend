package dk.minkostplan.backend.linerunners;

import dk.minkostplan.backend.service.MetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

//@Component
public class SetupMetasLoader implements CommandLineRunner {

    private final MetaService metaService;

    @Autowired
    public SetupMetasLoader(MetaService metaService) {
        this.metaService = metaService;
    }

    @Override
    public void run(String... args) throws Exception {
        metaService.createMeta("skåret");
    }
}
