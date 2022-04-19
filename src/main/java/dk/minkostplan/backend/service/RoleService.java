package dk.minkostplan.backend.service;

import dk.minkostplan.backend.entities.Role;
import dk.minkostplan.backend.models.ERole;
import dk.minkostplan.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Role getRoleByName(ERole eRole) {
        Role role = roleRepository.findByName(eRole);
        if (role == null) {
            role = new Role(eRole);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    public void createRoleIfNotFound(ERole eRole) {
        if (roleRepository.existsByName(eRole)) return;
        Role role = new Role(eRole);
        roleRepository.save(role);
    }
}
