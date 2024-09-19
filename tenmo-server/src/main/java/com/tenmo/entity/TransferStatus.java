package com.tenmo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "transfer_status")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferStatus {

    private Integer transferStatusId;

    private String statusDescription;

    private LocalDateTime createdAt;

    public TransferStatus(String statusDescription){
        this.statusDescription = statusDescription;
        createdAt = LocalDateTime.now();
    }
}
