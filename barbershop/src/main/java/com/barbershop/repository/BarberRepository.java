package com.barbershop.repository;

import com.barbershop.model.Barber;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class BarberRepository {
    private final JdbcClient jdbcClient;

    public BarberRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Barber> findAll() {
        return jdbcClient.sql("""
                SELECT b.barber_id, p.first_name, p.last_name, p.email, b.license_no, b.role
                FROM barber b
                JOIN person p ON b.barber_id = p.person_id
                """).query(Barber.class).list();
    }

    public Optional<Barber> findById(Long id) {
        String sql = """
                SELECT p.person_id as barberId, p.first_name, p.last_name, p.email, b.license_no, b.role
                FROM barber b
                JOIN person p ON b.barber_id = p.person_id
                WHERE b.barber_id = ?
                """;
        return jdbcClient.sql(sql)
                .param(id)
                .query(Barber.class)
                .optional();
    }
}