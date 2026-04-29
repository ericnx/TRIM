package com.barbershop.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "admin_id", nullable = false)
    private Barber admin;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "staff_id", nullable = false)
    private Barber staff;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    @Column(name = "shift_start_time", nullable = false)
    private LocalTime shiftStartTime;

    @Column(name = "shift_end_time", nullable = false)
    private LocalTime shiftEndTime;

    public Schedule() {
    }

    public Schedule(Barber admin, Barber staff, String dayOfWeek,
            LocalTime shiftStartTime, LocalTime shiftEndTime) {
        this.admin = admin;
        this.staff = staff;
        this.dayOfWeek = dayOfWeek;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Barber getAdmin() {
        return admin;
    }

    public Barber getStaff() {
        return staff;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getShiftStartTime() {
        return shiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return shiftEndTime;
    }

    public void setScheduleId(Long id) {
        this.scheduleId = id;
    }

    public void setAdmin(Barber b) {
        this.admin = b;
    }

    public void setStaff(Barber b) {
        this.staff = b;
    }

    public void setDayOfWeek(String d) {
        this.dayOfWeek = d;
    }

    public void setShiftStartTime(LocalTime t) {
        this.shiftStartTime = t;
    }

    public void setShiftEndTime(LocalTime t) {
        this.shiftEndTime = t;
    }
}