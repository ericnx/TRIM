package com.barbershop.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class Appointment {

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    private Long clientId;
    private Long serviceId;
    private Long scheduleId;
    private LocalTime slotStartTime;
    private LocalDate date;
    private Status status;
    private BigDecimal currentPrice;

    public Appointment() {
    }

    public Appointment(Long clientId, Long serviceId, Long scheduleId, LocalTime slotStartTime, LocalDate date,
            Status status, BigDecimal currentPrice) {
        this.clientId = clientId;
        this.serviceId = serviceId;
        this.scheduleId = scheduleId;
        this.slotStartTime = slotStartTime;
        this.date = date;
        this.status = status;
        this.currentPrice = currentPrice;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
}