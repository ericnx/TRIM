package com.barbershop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class AppointmentId implements Serializable {
    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "slot_start_time")
    private LocalTime slotStartTime;

    @Column(name = "date")
    private LocalDate date;

    public AppointmentId() {
    }

    public AppointmentId(Long clientId, Long scheduleId, LocalTime slotStartTime, LocalDate date) {
        this.clientId = clientId;
        this.scheduleId = scheduleId;
        this.slotStartTime = slotStartTime;
        this.date = date;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public LocalTime getSlotStartTime() {
        return slotStartTime;
    }

    public void setSlotStartTime(LocalTime slotStartTime) {
        this.slotStartTime = slotStartTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AppointmentId that = (AppointmentId) o;
        return Objects.equals(clientId, that.clientId) &&
                Objects.equals(scheduleId, that.scheduleId) &&
                Objects.equals(slotStartTime, that.slotStartTime) &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, scheduleId, slotStartTime, date);
    }
}