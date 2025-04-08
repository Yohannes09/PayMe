package com.payme.internal_authentication.repository;

import com.payme.internal_authentication.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    @Query("""
            SELECT client FROM Client client
            WHERE client.baseUrl = :baseUrl
            """)
    Optional<Client> findClientByBaseUrl(@Param("baseUrl") String baseUrl);
}
