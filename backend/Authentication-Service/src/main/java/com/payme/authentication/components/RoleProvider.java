package com.payme.authentication.components;

import com.payme.authentication.entity.Role;
import com.payme.authentication.exception.RoleNotFoundException;
import com.payme.authentication.repository.RoleRepository;
import com.payme.internal.security.constant.PaymeRoles;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides access to application roles and ensures their presence at startup.
 * <p>
 * On initialization, this class loads all predefined {@link PaymeRoles} into the database
 * (if missing) and caches them for fast retrieval.
 * </p>
 *
 * <p>Use {@link #findRole(PaymeRoles)} to fetch a role from the in-memory cache.</p>
 *
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class RoleProvider {
    private final RoleRepository roleRepository;
    private final Map<PaymeRoles, Role> roleCache = new HashMap<>();


    @PostConstruct
    private void addAllRoles(){
        List<Role> allRoles = PaymeRoles.getAll()
                .stream()
                .map(Role::new)
                .toList();

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

    public Role findRole(PaymeRoles role){
        return Optional.ofNullable(roleCache.get(role))
                .orElseThrow(() -> new RoleNotFoundException("Could not find role: " + role.getRole()));
    }

}
