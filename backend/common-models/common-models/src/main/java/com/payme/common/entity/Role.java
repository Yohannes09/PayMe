package com.payme.common.entity;

import com.payme.common.constants.PaymeRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "role")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_sequence")
    @SequenceGenerator(name = "role_id_sequence", sequenceName = "role_id_sequence", initialValue = 3456)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(updatable = true, nullable = false)
    private PaymeRoles role;

    public Role(PaymeRoles role){
        this.role = role;
    }
}
