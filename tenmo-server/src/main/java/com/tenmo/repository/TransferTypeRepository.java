package com.tenmo.repository;

import com.tenmo.entity.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransferTypeRepository extends JpaRepository<TransferType, Integer> {
    @Query(value = "SELECT * FROM transfer_type tt " +
            "WHERE LOWER(tt.transfer_type_desc) = LOWER(:typeDescription)", nativeQuery = true)
    Optional<TransferType> findByDescription(@Param("typeDescription")String typeDescription);
}
