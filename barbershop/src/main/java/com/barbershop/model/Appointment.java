package com.barbershop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "appointment")
public class Appointment {

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false)
    private BarberService service;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", nullable = false, unique = true)
    private Slot slot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "current_price", nullable = false, precision = 8, scale = 2)
    private BigDecimal currentPrice;

    public Appointment() {
    }

    public Appointment(Client client, BarberService service, Schedule schedule,
            Slot slot, Status status, BigDecimal currentPrice) {
        this.client = client;
        this.service = service;
        this.schedule = schedule;
        this.slot = slot;
        this.status = status;
        this.currentPrice = currentPrice;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public Client getClient() {
        return client;
    }

    public BarberService getService() {
        return service;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Slot getSlot() {
        return slot;
    }

    public Status getStatus() {
        return status;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setAppointmentId(Long id) {
        this.appointmentId = id;
    }

    public void setClient(Client c) {
        this.client = c;
    }

    public void setService(BarberService s) {
        this.service = s;
    }

    public void setSchedule(Schedule s) {
        this.schedule = s;
    }

    public void setSlot(Slot s) {
        this.slot = s;
    }

    public void setStatus(Status s) {
        this.status = s;
    }

    public void setCurrentPrice(BigDecimal p) {
        this.currentPrice = p;
    }
}