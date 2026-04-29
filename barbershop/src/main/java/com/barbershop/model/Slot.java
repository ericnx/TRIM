package com.barbershop.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "slot", uniqueConstraints = @UniqueConstraint(columnNames = { "schedule_id", "slot_start_time" }))
public class Slot {

    public enum Status {
        AVAILABLE, BOOKED, UNAVAILABLE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Long slotId;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @Column(name = "slot_start_time", nullable = false)
    private LocalTime slotStartTime;

    @Column(name = "slot_end_time")
    private LocalTime slotEndTime;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public Slot() {
    }

    public Slot(Schedule schedule, LocalTime slotStartTime,
            LocalTime slotEndTime, LocalDate date, Status status) {
        this.schedule = schedule;
        this.slotStartTime = slotStartTime;
        this.slotEndTime = slotEndTime;
        this.date = date;
        this.status = status;
    }

    public Long getSlotId() {
        return slotId;
    }

    public Long getVersion() {
        return version;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public LocalTime getSlotStartTime() {
        return slotStartTime;
    }

    public LocalTime getSlotEndTime() {
        return slotEndTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public Status getStatus() {
        return status;
    }

    public void setSlotId(Long id) {
        this.slotId = id;
    }

    public void setVersion(Long v) {
        this.version = v;
    }

    public void setSchedule(Schedule s) {
        this.schedule = s;
    }

    public void setSlotStartTime(LocalTime t) {
        this.slotStartTime = t;
    }

    public void setSlotEndTime(LocalTime t) {
        this.slotEndTime = t;
    }

    public void setDate(LocalDate d) {
        this.date = d;
    }

    public void setStatus(Status s) {
        this.status = s;
    }
}