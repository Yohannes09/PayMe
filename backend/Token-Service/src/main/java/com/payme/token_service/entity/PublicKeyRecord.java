package com.payme.token_provider.entity;

import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "public_key_record")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicKeyRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Lob
    @Column(nullable = false, updatable = false, unique = true)
    private String publicKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private SignatureAlgorithm signatureAlgorithm;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;
}