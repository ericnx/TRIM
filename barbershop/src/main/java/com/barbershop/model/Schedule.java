package com.barbershop.model;

import java.time.LocalTime;

public class Schedule {

    private Long scheduleId;
    private Long adminId;
    private Long staffId;
    private String dayOfWeek;
    private LocalTime shiftStartTime;
    private LocalTime shiftEndTime;

    public Schedule() {
    }

    public Schedule(Long adminId, Long staffId, String dayOfWeek,
            LocalTime shiftStartTime, LocalTime shiftEndTime) {
        this.adminId = adminId;
        this.staffId = staffId;
        this.dayOfWeek = dayOfWeek;
        this.shiftStartTime = shiftStartTime;
        this.shiftEndTime = shiftEndTime;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getShiftStartTime() {
        return shiftStartTime;
    }

    public void setShiftStartTime(LocalTime shiftStartTime) {
        this.shiftStartTime = shiftStartTime;
    }

    public LocalTime getShiftEndTime() {
        return shiftEndTime;
    }

    public void setShiftEndTime(LocalTime shiftEndTime) {
        this.shiftEndTime = shiftEndTime;
    }
}