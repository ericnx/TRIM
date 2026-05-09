package com.barbershop.repository;

import com.barbershop.model.Schedule;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleRepository {

    private final JdbcClient jdbcClient;

    public ScheduleRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Schedule> findAll() {
        return jdbcClient.sql("SELECT * FROM schedule")
                .query(Schedule.class)
                .list();
    }

    public Optional<Schedule> findById(Long id) {
        return jdbcClient.sql("SELECT * FROM schedule WHERE schedule_id = ?")
                .param(id)
                .query(Schedule.class)
                .optional();
    }

    public Optional<Schedule> findByStaffAndDay(Long staffId, String dayOfWeek) {
        return jdbcClient.sql("SELECT * FROM schedule WHERE staff_id = ? AND day_of_week = ?")
                .param(staffId)
                .param(dayOfWeek)
                .query(Schedule.class)
                .optional();
    }
}