package com.payme.token_provider.entity;

import com.payme.token_provider.constant.KeyAlgorithm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "signing_key")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SigningKey {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Lob
    @Column(nullable = false, updatable = false, unique = true)
    private String publicKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private KeyAlgorithm keyAlgorithm;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime revokedAt;
}
