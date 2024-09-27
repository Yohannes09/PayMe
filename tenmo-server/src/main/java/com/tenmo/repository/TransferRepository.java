package com.tenmo.repository;

import com.tenmo.dto.transfer.TransferResponseDto;
import com.tenmo.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Query(value = "SELECT * FROM transfer tr " +
            "JOIN transfer_type tt ON tt.transfer_type_id = tr.transfer_type_id " +
            "JOIN transfer_status ts ON ts.transfer_status_id = tr.transfer_status_id " +
            "WHERE LOWER(tt.transfer_type_desc) = 'request' " +
            "AND LOWER(ts.transfer_status_desc) = 'approved' ", nativeQuery = true)
    List<Transfer> getApprovedRequests();


    @Modifying
    @Query(value = "UPDATE transfer t " +
            "SET t.transferStatusId = :newTransferStatusId " +
            "WHERE t.transferId = :transferId",
            nativeQuery = true)
    Optional<Transfer> updateTransferStatus(@Param("transferId") Long transferId,
                                  @Param("newTransferStatusId") Integer newTransferStatusId
    );


}
