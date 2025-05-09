package com.payme.authentication.repository;

import com.payme.authentication.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(PaymeRoles role);

    boolean existsByRole(PaymeRoles role);
}
