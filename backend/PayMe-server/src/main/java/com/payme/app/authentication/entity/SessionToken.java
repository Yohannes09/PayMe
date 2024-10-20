package com.payme.app.authentication.entity;

import com.payme.app.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

/**
 * Stores user's tokens. Allows logout feature and invalidation of tokens manually*/

@Builder
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "session_tokens")
public class SessionToken {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID tokenId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @NotNull
    @Column(name = "token", nullable = false, updatable = false, length = 512)
    private String token;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "expires_at", nullable = false, updatable = false)
    private Date expiresAt;
}
