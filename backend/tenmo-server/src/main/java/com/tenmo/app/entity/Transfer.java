package com.tenmo.app.entity;

import com.tenmo.app.util.Currency;
import com.tenmo.app.util.TransferStatus;
import com.tenmo.app.util.TransferType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "transfer", indexes = {
        @Index(name = "idx_account_from", columnList = "account_from"),
        @Index(name = "idx_account_to", columnList = "account_to"),
        @Index(name = "idx_transfer_id", columnList = "transfer_id")
})
@Entity
public class Transfer {

    @Id
    @Column(name = "transfer_id", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    private UUID transferId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_type_id", nullable = false)
    private TransferType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_status_id", nullable = false)
    private TransferStatus status;

    @NotNull
    @Column(name = "account_from", nullable = false)
    private UUID accountFrom;

    @Column(name = "account_to", nullable = false)
    private @NotNull List<UUID> accountTo;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transfer_message", length = 255)
    private String transferMessage;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

}