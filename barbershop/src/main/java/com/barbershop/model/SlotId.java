package com.barbershop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Embeddable
public class SlotId implements Serializable {

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "slot_start_time")
    private LocalTime slotStartTime;

    @Column(name = "date")
    private LocalDate date;

    public SlotId() {
    }

    public SlotId(Long scheduleId, LocalTime slotStartTime, LocalDate date) {
        this.scheduleId = scheduleId;
        this.slotStartTime = slotStartTime;
        this.date = date;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public LocalTime getSlotStartTime() {
        return slotStartTime;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SlotId slotId = (SlotId) o;
        return Objects.equals(scheduleId, slotId.scheduleId) &&
                Objects.equals(slotStartTime, slotId.slotStartTime) &&
                Objects.equals(date, slotId.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, slotStartTime, date);
    }
}