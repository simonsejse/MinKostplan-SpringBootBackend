package dk.minkostplan.backend.linerunners;

import dk.minkostplan.backend.models.ERole;
import dk.minkostplan.backend.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.logging.Logger;

@Component
public class SetupRolesLoader implements CommandLineRunner {

    private final Logger log = Logger.getLogger(SetupRolesLoader.class.getName());

    private final RoleService roleService;

    @Autowired
    public SetupRolesLoader(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("Checking and creating roles depending if they exists..");
        Arrays.stream(ERole.values()).forEach(roleService::createRoleIfNotFound);
    }
}
