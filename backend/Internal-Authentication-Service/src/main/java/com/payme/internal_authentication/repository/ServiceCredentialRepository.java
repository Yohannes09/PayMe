package com.payme.internal_authentication.repository;

import com.payme.internal_authentication.entity.ServiceCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ServiceCredentialRepository extends JpaRepository<ServiceCredential, UUID> {

}
