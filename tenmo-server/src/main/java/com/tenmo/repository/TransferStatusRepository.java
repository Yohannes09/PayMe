package com.tenmo.repository;

import com.tenmo.entity.TransferStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferStatusRepository extends JpaRepository<TransferStatus, Integer> {
    @Query(value = "SELECT * FROM transfer_status " +
            "WHERE LOWER(transfer_status_desc) = LOWER(:statusDescription); ", nativeQuery = true)
    Optional<TransferStatus> findByDescription(@Param("statusDescription")String statusDescription);
}
