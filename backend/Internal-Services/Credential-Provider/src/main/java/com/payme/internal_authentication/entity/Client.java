package com.payme.internal_authentication.entity;

import com.payme.internal_authentication.constant.ServiceType;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    @Column(name = "base_url", unique = true, nullable = false)
    private String baseUrl;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToMany(
            mappedBy = "client",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Credential> credentials;

}
