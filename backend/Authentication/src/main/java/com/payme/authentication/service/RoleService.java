package com.payme.authentication.service;

import com.payme.authentication.constant.PaymeRoles;
import com.payme.authentication.entity.Role;
import com.payme.authentication.exception.RoleNotFoundException;
import com.payme.authentication.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/* Instead of looking up a role from the db each time, cache it locally
*  AuthServices no longer have to look up roles from the db when registering.
*  */

@Slf4j
@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final Map<PaymeRoles, Role> roleCache = new HashMap<>();

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @PostConstruct
    private void addAllroles(){
        List<Role> allRoles = PaymeRoles.allRoles()
                .stream()
                .map(Role::new)
                .toList();

        // Fetch all stored roles and add them to a set for fast lookups (slight optimization)
        // very few roles
        Set<PaymeRoles> storedRoles = roleRepository.findAll()
                .stream()
                .map(Role::getRole)
                .collect(Collectors.toSet());

        allRoles.forEach(role -> {
            if (!storedRoles.contains(role.getRole())){
                roleRepository.save(role);
                log.info("New role saved: {}", role.getRole());
            }else {
                log.info("Role already exists: {}", role.getRole());
            }
            roleCache.put(role.getRole(), role);
        });

        log.info("Roles added to cache.");
    }

    public Role fetchRole(PaymeRoles role){
        return Optional.ofNullable(roleCache.get(role))
                .orElseThrow(() -> new RoleNotFoundException("Could not find role: " + role.getRole()));
    }

}
