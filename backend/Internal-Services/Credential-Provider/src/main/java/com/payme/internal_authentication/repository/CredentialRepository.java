package com.payme.internal_authentication.repository;

import com.payme.internal_authentication.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CredentialRepository extends JpaRepository<Credential, UUID> {

}
