package com.myboard.userservice.components;

import com.myboard.userservice.entity.Role;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.RoleRepository;
import com.myboard.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitialization {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        Role adminRole = new Role("ROLE_ADMIN");
        Role userRole = new Role("ROLE_USER");
        roleRepository.save(adminRole);
        roleRepository.save(userRole);

        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminRoles.add(userRole);

        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);

        User admin = new User();
        admin.setUsername("admin");
        admin.setUsername("admin");
        admin.setRoles(adminRoles);
        User user = new User();
        user.setUsername("admin");
        user.setUsername("admin");
        user.setRoles(userRoles);

        userRepository.save(admin);
        userRepository.save(user);
    }
}
