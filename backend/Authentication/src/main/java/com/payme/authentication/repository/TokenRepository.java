package com.payme.authentication.repository;

import com.payme.authentication.entity.Token;
import com.payme.authentication.user_micro_serv.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    @Query("""
            SELECT token FROM SessionToken token
            WHERE token.user.userId = :userId
            AND token.expiresAt > CURRENT_TIMESTAMP
            ORDER BY token.createdAt DESC
            """)
    List<Token> findAllByUserId(UUID userId);

    Optional<Token> findByToken(String token);

    void deleteByUser(User user);

    void deleteByToken(String token);

}
