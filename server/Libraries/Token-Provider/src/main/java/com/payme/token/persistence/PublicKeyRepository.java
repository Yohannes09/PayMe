package com.payme.token.persistence;

import com.payme.token.model.PublicKeyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublicKeyRepository extends
        JpaRepository<PublicKeyRecord, UUID> ,
        PublicKeyStore<PublicKeyRecord> {

}
