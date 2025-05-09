package com.payme.token_service.repository;

import com.payme.token_service.entity.PublicKeyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PublicKeyRecordRepository extends JpaRepository<PublicKeyRecord, UUID> {
}
