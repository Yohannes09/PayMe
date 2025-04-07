package com.payme.internal_authentication.entity;

import com.payme.internal_authentication.constant.ServiceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "serviceClient")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceClient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "service_name", unique = true, nullable = false)
    private String serviceName;

    @Column(name = "base_url", nullable = false)
    private String baseUrl;

    @Column(name = "endpoint", nullable = false)
    private String endpoint;

    private String description;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @OneToMany(
            mappedBy = "serviceClient",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<ServiceCredential> serviceCredential;

}
