package com.payme.token_provider.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private String token;

    @Column(nullable = false, updatable = false)
    private String tokenSubject;

    @Column(nullable = false)
    private Instant expiryDate;

    private boolean revoked;
}
