package com.payme.token.repository;

import com.payme.token.model.entity.PublicKeyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublicKeyRepository extends JpaRepository<PublicKeyRecord, UUID> ,
        PublicKeyStore<PublicKeyRecord>{
}
