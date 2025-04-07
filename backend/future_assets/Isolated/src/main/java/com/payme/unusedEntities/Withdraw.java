//package com.payme.app.entity;
//
//import com.payme.app.diffMicroServ.constants.TransactionStatus;
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.GenericGenerator;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Builder
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "withdraws")
//public class Withdraw {
//    @Id
//    @GeneratedValue(generator = "UUID")
//    @GenericGenerator(
//            name = "UUID",
//            strategy = "org.hibernate.id.UUIDGenerator"
//    )
//    private UUID withdrawId;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "userId", nullable = false)
//    private User user;
//
//    @NotNull
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "accountId", nullable = false)
//    private Account account;
//
//    @NotNull
//    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
//    private BigDecimal amount;
//
//    @NotNull
//    @Enumerated(EnumType.STRING)
//    private TransactionStatus status;
//
//    @NotNull
//    @CreationTimestamp
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime initiatedAt;
//
//    @Column(updatable = false)
//    @UpdateTimestamp
//    private LocalDateTime completedAt;
//}
