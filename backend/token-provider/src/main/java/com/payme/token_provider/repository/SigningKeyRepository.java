package com.payme.token_provider.repository;

import com.payme.token_provider.entity.SigningKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SigningKeyRepository extends JpaRepository<SigningKey, UUID> {
}
