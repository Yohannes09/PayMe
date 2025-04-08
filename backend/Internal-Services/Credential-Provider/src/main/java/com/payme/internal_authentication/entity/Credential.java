package com.payme.internal_authentication.entity;

import com.payme.internal_authentication.constant.CredentialStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "credential")
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String credential;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Client client;

    @Enumerated(EnumType.STRING)
    private CredentialStatus credentialStatus;

    @CreationTimestamp
    private LocalDateTime issuedAt;

    private LocalDateTime expiresAt;
}
