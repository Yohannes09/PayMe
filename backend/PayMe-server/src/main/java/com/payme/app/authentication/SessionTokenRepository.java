package com.payme.app.authentication;

import com.payme.app.authentication.entity.SessionToken;
import com.payme.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SessionTokenRepository extends JpaRepository<SessionToken, UUID> {

    Optional<SessionToken> findByUser(User user);

    Optional<SessionToken> findByToken(String token);

    void delteteByUser(User user);

    void deleteByToken(String token);

}
