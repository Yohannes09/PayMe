package com.payme.authentication.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "token")
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID tokenId;

    @ManyToOne
    @JoinColumn(name = "security_user_id", nullable = false)
    private SecurityUser securityUser;

    @Column(name = "token", nullable = false, updatable = false, length = 512)
    private String token;

    @Column(name = "is_valid", nullable = false)
    private boolean isValid = true;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private Date expiresAt;

    public void invalidateToken(){
        this.isValid = false;
    }
}


