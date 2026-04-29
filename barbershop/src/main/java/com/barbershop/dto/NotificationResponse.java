package com.barbershop.dto;

public class NotificationResponse {

    private String notificationId;
    private String status;
    private int emailsSent;
    private String message;

    public NotificationResponse() {
    }

    public NotificationResponse(String notificationId, String status,
            int emailsSent, String message) {
        this.notificationId = notificationId;
        this.status = status;
        this.emailsSent = emailsSent;
        this.message = message;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public String getStatus() {
        return status;
    }

    public int getEmailsSent() {
        return emailsSent;
    }

    public String getMessage() {
        return message;
    }

    public void setNotificationId(String s) {
        this.notificationId = s;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public void setEmailsSent(int n) {
        this.emailsSent = n;
    }

    public void setMessage(String s) {
        this.message = s;
    }
}