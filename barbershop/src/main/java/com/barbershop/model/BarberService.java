package com.barbershop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "service")
public class BarberService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    public BarberService() {
    }

    public BarberService(String type, BigDecimal price, Integer duration) {
        this.type = type;
        this.price = price;
        this.duration = duration;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setServiceId(Long id) {
        this.serviceId = id;
    }

    public void setType(String t) {
        this.type = t;
    }

    public void setPrice(BigDecimal p) {
        this.price = p;
    }

    public void setDuration(Integer d) {
        this.duration = d;
    }
}