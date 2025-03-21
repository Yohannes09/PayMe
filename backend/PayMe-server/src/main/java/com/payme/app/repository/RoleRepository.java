package com.payme.app.repository;

import com.payme.app.constants.PaymeRoles;
import com.payme.app.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(PaymeRoles role);
}
