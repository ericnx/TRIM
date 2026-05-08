package com.barbershop.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "appointment")
public class Appointment {

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    @EmbeddedId
    private AppointmentId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("clientId")
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "service_id", nullable = false)
    private BarberService service;

@ManyToOne
@JoinColumns({
    @JoinColumn(name = "schedule_id", referencedColumnName = "schedule_id", insertable = false, updatable = false),
    @JoinColumn(name = "slot_start_time", referencedColumnName = "slot_start_time", insertable = false, updatable = false),
    @JoinColumn(name = "date", referencedColumnName = "date", insertable = false, updatable = false)
})
private Slot slot;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "current_price", nullable = false, precision = 8, scale = 2)
    private BigDecimal currentPrice;

    public Appointment() {
    }

    public Appointment(Client client, Slot slot, BarberService service, Status status, BigDecimal currentPrice) {
        this.id = new AppointmentId(
                client.getClientId(),
                slot.getSchedule().getScheduleId(),
                slot.getSlotStartTime(),
                slot.getDate());
        this.client = client;
        this.slot = slot;
        this.service = service;
        this.status = status;
        this.currentPrice = currentPrice;
    }

    public AppointmentId getId() {
        return id;
    }

    public void setId(AppointmentId id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public BarberService getService() {
        return service;
    }

    public void setService(BarberService service) {
        this.service = service;
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