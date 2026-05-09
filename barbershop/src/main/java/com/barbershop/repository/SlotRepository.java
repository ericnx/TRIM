package com.barbershop.repository;

import com.barbershop.model.Slot;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public class SlotRepository {

    private final JdbcClient jdbcClient;

    public SlotRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Slot> findAvailableByDate(LocalDate date) {
        String sql = """
                SELECT schedule_id, slot_start_time, date, slot_end_time, status, version
                FROM slot
                WHERE date = ? AND status = 'AVAILABLE'
                ORDER BY slot_start_time ASC
                """;

        return jdbcClient.sql(sql)
                .param(date)
                .query(Slot.class)
                .list();
    }

    public boolean reserveSlot(Long scheduleId, LocalDate date, LocalTime startTime) {
        String sql = """
                UPDATE slot
                SET status = 'BOOKED'
                WHERE schedule_id = :scheduleId
                  AND date = :date
                  AND slot_start_time = :startTime
                  AND status = 'AVAILABLE'
                """;

        int rowsAffected = jdbcClient.sql(sql)
                .param("scheduleId", scheduleId)
                .param("date", date)
                .param("startTime", startTime)
                .update();

        return rowsAffected > 0;
    }

    public boolean checkConnection() {
        try {
            Integer result = jdbcClient.sql("SELECT 1")
                    .query(Integer.class)
                    .single();
            return result != null && result == 1;
        } catch (Exception e) {
            return false;
        }
    }
}