package com.payme.internal_authentication.repository;

import com.payme.internal_authentication.entity.ServiceClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ServiceClientRepository extends JpaRepository<ServiceClient, UUID> {

    Optional<ServiceClient> findByServiceName(String serviceName);
}
