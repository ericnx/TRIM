package com.barbershop.repository;

import com.barbershop.model.Appointment;
import com.barbershop.model.AppointmentView;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class AppointmentRepository {

    private final JdbcClient jdbcClient;

    public AppointmentRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<AppointmentView> findAll() {
        String sql = """
                SELECT
                    CONCAT(cp.first_name, ' ', cp.last_name)  AS client_name,
                    CONCAT(bp.first_name, ' ', bp.last_name)  AS barber_name,
                    bs.type                                    AS service_type,
                    sc.day_of_week,
                    a.slot_start_time,
                    a.current_price,
                    a.status
                FROM appointment a
                JOIN client      c   ON c.client_id  = a.client_id
                JOIN person      cp  ON cp.person_id = c.client_id
                JOIN barber_service bs ON bs.service_id = a.service_id
                JOIN schedule    sc  ON sc.schedule_id = a.schedule_id
                JOIN barber      b   ON b.barber_id  = sc.staff_id
                JOIN person      bp  ON bp.person_id = b.barber_id
                ORDER BY sc.day_of_week, a.slot_start_time
                """;

        return jdbcClient.sql(sql)
                .query((rs, rowNum) -> {
                    AppointmentView view = new AppointmentView();
                    view.setClientName(rs.getString("client_name"));
                    view.setBarberName(rs.getString("barber_name"));
                    view.setServiceType(rs.getString("service_type"));
                    view.setDayOfWeek(rs.getString("day_of_week"));
                    view.setSlotStartTime(rs.getTime("slot_start_time").toLocalTime());
                    view.setCurrentPrice(rs.getBigDecimal("current_price"));
                    view.setStatus(rs.getString("status"));
                    return view;
                })
                .list();
    }

    public Appointment save(Appointment appointment) {
        String sql = """
                INSERT INTO appointment (client_id, service_id, schedule_id, slot_id, status, current_price)
                VALUES (:clientId, :serviceId, :scheduleId, :slotId, :status, :currentPrice)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("clientId", appointment.getClient().getPersonId())
                .param("serviceId", appointment.getService().getServiceId())
                .param("scheduleId", appointment.getSchedule().getScheduleId())
                .param("slotId", appointment.getSlot().getSlotId())
                .param("status", appointment.getStatus().name())
                .param("currentPrice", appointment.getCurrentPrice())
                .update(keyHolder, "appointment_id");

        appointment.setAppointmentId(keyHolder.getKey().longValue());
        return appointment;
    }
}
