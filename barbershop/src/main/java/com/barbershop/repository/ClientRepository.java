package com.barbershop.repository;

import com.barbershop.model.Client;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class ClientRepository {

    private final JdbcClient jdbcClient;

    public ClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<Client> findByEmail(String email) {
        String sql = """
                SELECT p.person_id as clientId, p.first_name, p.last_name, p.email, p.phone 
                FROM client c 
                JOIN person p ON c.client_id = p.person_id 
                WHERE p.email = ?
                """;
        return jdbcClient.sql(sql)
                .param(email)
                .query(Client.class)
                .optional();
    }

    public Optional<Client> findById(Long id) {
        String sql = """
                SELECT p.person_id as clientId, p.first_name, p.last_name, p.email, p.phone 
                FROM client c 
                JOIN person p ON c.client_id = p.person_id 
                WHERE c.client_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Client.class)
                .optional();
    }

    public Client saveAndFlush(Client client) {
        return client;
    }
}