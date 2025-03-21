package com.payme.app.entity;

import com.payme.app.constants.PaymeRoles;
import jakarta.persistence.*;
import lombok.Getter;

@Table(name = "roles")
@Entity
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(updatable = true, nullable = false)
    private PaymeRoles role;
}
