package com.payme.internal_authentication.entity;

import com.payme.internal_authentication.constant.ServiceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "client")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "secret", nullable = false)
    private String secret;

    @Column(name = "base_url", nullable = false, unique = true)
    private String baseUrl;

    @Column()
    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    List<ClientEndpoint> endpoints;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    private LocalDateTime modifiedAt;

    @Column(name = "last_authenticated_at")
    private LocalDateTime lastAuthenticatedAt;

    @Column(name = "last_issued_token_at")
    private LocalDateTime lastIssuedTokenAt;
}
