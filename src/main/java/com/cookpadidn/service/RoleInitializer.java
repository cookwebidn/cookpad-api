package com.cookpadidn.service;

import com.cookpadidn.entity.Role;
import com.cookpadidn.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    private final Logger log =  LoggerFactory.getLogger(RoleInitializer.class);

    @Override
    public void run(String... args) throws Exception {

        final String ROLE_USER = "ROLE_USER";
        final String ROLE_ADMIN = "ROLE_ADMIN";

        Optional<Role> userRole = roleRepository.findRoleByName(ROLE_USER);
        Optional<Role> adminRole = roleRepository.findRoleByName(ROLE_ADMIN);

        if (userRole.isEmpty()) {
            Role user = Role.builder().name(ROLE_USER).build();
            roleRepository.save(user);
            log.info("successfully created Role Name = {}", ROLE_USER);
        }

        if (adminRole.isEmpty()) {
            Role admin = Role.builder().name(ROLE_ADMIN).build();
            roleRepository.save(admin);
            log.info("successfully created Role Name = {}", ROLE_ADMIN);
        }
    }
}
