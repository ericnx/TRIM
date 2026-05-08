package com.barbershop.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "slot")
public class Slot {

    public enum Status {
        AVAILABLE, BOOKED, UNAVAILABLE
    }

    @EmbeddedId
    private SlotId id;

    @Version
    private Integer version;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("scheduleId")
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "slot_end_time")
    private LocalTime slotEndTime;

    public Slot() {
    }

    public Slot(Schedule schedule, LocalTime slotStartTime, LocalTime slotEndTime, LocalDate date, Status status) {
        this.id = new SlotId(schedule.getScheduleId(), slotStartTime, date);
        this.schedule = schedule;
        this.slotEndTime = slotEndTime;
        this.status = status;
    }

    public LocalTime getSlotStartTime() {
        return id != null ? id.getSlotStartTime() : null;
    }

    public LocalDate getDate() {
        return id != null ? id.getDate() : null;
    }

    public SlotId getId() {
        return id;
    }

    public void setId(SlotId id) {
        this.id = id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public LocalTime getSlotEndTime() {
        return slotEndTime;
    }

    public void setSlotEndTime(LocalTime slotEndTime) {
        this.slotEndTime = slotEndTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

    public Integer getVersion() {
        return version;
    }
}