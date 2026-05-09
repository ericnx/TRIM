package com.barbershop.model;

import java.math.BigDecimal;

public class BarberService {

    private Long serviceId;
    private Long barberId;
    private String type;
    private BigDecimal price;
    private Integer duration;

    public BarberService() {
    }

    public BarberService(Long barberId, String type, BigDecimal price, Integer duration) {
        this.barberId = barberId;
        this.type = type;
        this.price = price;
        this.duration = duration;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Long getBarberId() {
        return barberId;
    }

    public void setBarberId(Long barberId) {
        this.barberId = barberId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}