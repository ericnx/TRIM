package com.barbershop.dto;

public class NotificationRequest {

    private String clientName;
    private String clientEmail;
    private String barberName;
    private String barberEmail;
    private String serviceType;
    private String date;
    private String startTime;
    private String endTime;
    private String price;
    private Long appointmentId;

    public NotificationRequest() {
    }

    public NotificationRequest(String clientName, String clientEmail,
            String barberName, String barberEmail,
            String serviceType, String date,
            String startTime, String endTime,
            String price, Long appointmentId) {
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.barberName = barberName;
        this.barberEmail = barberEmail;
        this.serviceType = serviceType;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.appointmentId = appointmentId;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public String getBarberName() {
        return barberName;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPrice() {
        return price;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setClientName(String s) {
        this.clientName = s;
    }

    public void setClientEmail(String s) {
        this.clientEmail = s;
    }

    public void setBarberName(String s) {
        this.barberName = s;
    }

    public void setBarberEmail(String s) {
        this.barberEmail = s;
    }

    public void setServiceType(String s) {
        this.serviceType = s;
    }

    public void setDate(String s) {
        this.date = s;
    }

    public void setStartTime(String s) {
        this.startTime = s;
    }

    public void setEndTime(String s) {
        this.endTime = s;
    }

    public void setPrice(String s) {
        this.price = s;
    }

    public void setAppointmentId(Long id) {
        this.appointmentId = id;
    }
}