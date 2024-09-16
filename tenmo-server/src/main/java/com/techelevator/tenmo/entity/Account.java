package com.techelevator.tenmo.entity;

import com.techelevator.tenmo.util.AccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

//Lombok
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
// Jpa
@Table(name = "account")
@Entity
public class Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // JPA needs to know the DB will handle the Generation.
    private Long accountId;

    @Column(name = "user_id", nullable = false) // table row has underscore in the variable name hence the @Column annotation.
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;    // enum syntax for Postgres-> account_type ENUM('checking', 'savings', ...)

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
