package com.payme.authentication.repository;

import com.payme.authentication.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {
//    AND token.expiresAt > CURRENT_TIMESTAMP ORDER BY token.createdAt DESC
    @Query("""
            SELECT token FROM Token token
            WHERE token.securityUser.id = :id
            """)
    List<Token> findAllBySecurityUserId(@Param("id") UUID id);

    Optional<Token> findByToken(String token);

    void deleteByToken(String token);

}
