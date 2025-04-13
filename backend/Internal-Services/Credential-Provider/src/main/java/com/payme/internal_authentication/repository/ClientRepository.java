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
            WHERE (LOWER(client.name) = LOWER(:nameOrBaseUrl))
            OR (LOWER(client.baseUrl) = LOWER(:nameOrBaseUrl));
            """)
    Optional<Client> findByNameOrBaseUrl(@Param("nameOrBaseUrl") String nameOrBaseUrl);

    @Query("""
            SELECT CASE
                WHEN COUNT(client) > 0 THEN true
                ELSE false END
            FROM Client client
            WHERE (LOWER(client.name) = LOWER(:name))
            OR (LOWER(client.baseUrl) = LOWER(:baseUrl))
            """)
    boolean existsByNameOrBaseUrl(
            @Param("name") String name,
            @Param("baseUrl") String baseUrl
    );

}
