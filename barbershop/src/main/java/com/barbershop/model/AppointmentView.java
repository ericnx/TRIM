package com.barbershop.model;

import java.math.BigDecimal;
import java.time.LocalTime;

public class AppointmentView {

    private String clientName;
    private String barberName;
    private String serviceType;
    private String dayOfWeek;
    private LocalTime slotStartTime;
    private BigDecimal currentPrice;
    private String status;

    public AppointmentView() {
    }

    public String getClientName() {
        return clientName;
    }

    public String getBarberName() {
        return barberName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public LocalTime getSlotStartTime() {
        return slotStartTime;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public void setSlotStartTime(LocalTime t) {
        this.slotStartTime = t;
    }

    public void setCurrentPrice(BigDecimal price) {
        this.currentPrice = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
