package com.payme.app.entity;

import com.payme.app.util.Currency;
import com.payme.app.util.TransactionStatus;
import com.payme.app.util.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
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
public class Transaction {

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
    private TransactionType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "transfer_status_id", nullable = false)
    private TransactionStatus status;


    @Column(name = "account_from", nullable = false)
    @NotNull private UUID accountFrom;

    @Column(name = "account_to", nullable = false)
    @NotNull private List<UUID> accountTo;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Size(max = 255)
    @Column(name = "transfer_message", length = 255)
    private String transferMessage;

    @Size(max = 255)
    @Column(name = "failure_reason", updatable = false)
    private String failureReason;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency", nullable = false)
    private Currency currency;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}