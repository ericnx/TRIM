package com.barbershop.service;

import com.barbershop.dto.NotificationRequest;
import com.barbershop.dto.NotificationResponse;
import com.barbershop.model.Appointment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClient {
    private static final String NOTIFICATION_URL = "http://localhost:8080/api/notifications/booking-confirmation";
    private final RestTemplate restTemplate;

    public NotificationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String notifyBooking(Appointment appointment) {
        try {
            NotificationRequest request = new NotificationRequest(
                    appointment.getClient().getFullName(),
                    appointment.getClient().getEmail(),
                    appointment.getSchedule().getStaff().getFullName(),
                    appointment.getSchedule().getStaff().getEmail(),
                    appointment.getService().getType(),
                    appointment.getSlot().getDate().toString(),
                    appointment.getSlot().getSlotStartTime().toString(),
                    appointment.getSlot().getSlotEndTime().toString(),
                    appointment.getCurrentPrice().toString(),
                    appointment.getAppointmentId());

            ResponseEntity<NotificationResponse> response = restTemplate.postForEntity(
                    NOTIFICATION_URL,
                    request,
                    NotificationResponse.class);

            if (response.getBody() != null) {
                System.out.println("[NotificationClient] " +
                        response.getBody().getEmailsSent() + " emails sent. ID: " +
                        response.getBody().getNotificationId());
                return response.getBody().getNotificationId();
            }

        } catch (Exception e) {
            System.err.println("[NotificationClient] WARNING: " + e.getMessage());
        }
        return null;
    }
}