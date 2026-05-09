package com.barbershop.repository;

import com.barbershop.model.BarberService;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public class BarberServiceRepository {

    private final JdbcClient jdbcClient;

    public BarberServiceRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<BarberService> findAll() {
    String sql = """
                SELECT s.service_id, s.type, s.price, s.duration, m.barber_id 
                FROM services s
                JOIN barber_services_mapping m ON s.service_id = m.service_id
            """;

    return jdbcClient.sql(sql)
            .query((rs, rowNum) -> {
                BarberService service = new BarberService();
                service.setServiceId(rs.getLong("service_id"));
                service.setType(rs.getString("type"));
                service.setPrice(rs.getBigDecimal("price"));
                service.setDuration(rs.getLong("duration")); 
                service.setBarberId(rs.getLong("barber_id"));
                return service;
            }).list();
}

    public Optional<BarberService> findById(Long id) {
        return jdbcClient
                .sql("SELECT service_id as serviceId, type, price, duration FROM services WHERE service_id = ?")
                .param(id)
                .query(BarberService.class)
                .optional();
    }
}