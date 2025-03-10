package com.payme.app.authentication;

import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<SessionToken, UUID> {

    @Query("""
            SELECT token FROM SessionToken token
            WHERE token.user.userId = :userId
            """)
    @EntityGraph(attributePaths = {"user", "user.accounts"})
    Optional<SessionToken> findByUserId(UUID userId);

    Optional<SessionToken> findByToken(String token);

    void deleteByUser(User user);

    void deleteByToken(String token);

}
