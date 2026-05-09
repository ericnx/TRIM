package com.barbershop.service;

import com.barbershop.dto.NotificationRequest;
import com.barbershop.dto.NotificationResponse;
import com.barbershop.model.*;
import com.barbershop.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NotificationClient {
    private static final String NOTIFICATION_URL = "http://localhost:8080/api/notifications/booking-confirmation";
    private final RestTemplate restTemplate;
    private final ClientRepository clientRepo;
    private final BarberRepository barberRepo;
    private final BarberServiceRepository serviceRepo;

    public NotificationClient(RestTemplate restTemplate, 
                              ClientRepository clientRepo, 
                              BarberRepository barberRepo,
                              BarberServiceRepository serviceRepo) {
        this.restTemplate = restTemplate;
        this.clientRepo = clientRepo;
        this.barberRepo = barberRepo;
        this.serviceRepo = serviceRepo;
    }

    public String notifyBooking(Appointment appointment) {
        try {
            Client client = clientRepo.findById(appointment.getClientId()).orElseThrow();
            Barber barber = barberRepo.findById(appointment.getScheduleId()).orElseThrow();
            BarberService service = serviceRepo.findById(appointment.getServiceId()).orElseThrow();

            NotificationRequest request = new NotificationRequest(
                    client.getFirstName() + " " + client.getLastName(),
                    client.getEmail(),
                    barber.getFirstName() + " " + barber.getLastName(),
                    "barber@trim.com",
                    service.getType(),
                    String.valueOf(appointment.getDate()),
                    String.valueOf(appointment.getSlotStartTime()),
                    "N/A",
                    appointment.getCurrentPrice().toString(),
                    appointment.getClientId()
            );

            ResponseEntity<NotificationResponse> response = restTemplate.postForEntity(
                    NOTIFICATION_URL,
                    request,
                    NotificationResponse.class);

            if (response.getBody() != null) {
                return response.getBody().getNotificationId();
            }

        } catch (Exception e) {
            System.err.println("[NotificationClient] WARNING: External Service Unreachable: " + e.getMessage());
        }
        return null;
    }
}