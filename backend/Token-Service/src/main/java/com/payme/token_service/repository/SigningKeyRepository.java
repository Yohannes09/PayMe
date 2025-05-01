package com.payme.token_provider.repository;

import com.payme.token_provider.entity.PublicKeyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SigningKeyRepository extends JpaRepository<PublicKeyRecord, UUID> {
}
