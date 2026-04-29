package com.barbershop.controller;

import com.barbershop.dto.NotificationRequest;
import com.barbershop.dto.NotificationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @PostMapping("/booking-confirmation")
    public ResponseEntity<NotificationResponse> sendBookingConfirmation(
            @RequestBody NotificationRequest req) {

        String notifId = "NOTIF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                CLIENT CONFIRMATION               ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║ To:      " + padRight(req.getClientEmail(), 40) + "║");
        System.out.println("║ Subject: Your Trim Appointment is Confirmed      ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Hi " + padRight(req.getClientName() + ",", 45) + "║");
        System.out.println("║  Your appointment has been confirmed!            ║");
        System.out.println("║                                                  ║");
        System.out.println("║  Barber:  " + padRight(req.getBarberName(), 39) + "║");
        System.out.println("║  Service: " + padRight(req.getServiceType(), 39) + "║");
        System.out.println("║  Date:    " + padRight(req.getDate(), 39) + "║");
        System.out.println("║  Time:    " + padRight(req.getStartTime() + " – " + req.getEndTime(), 39) + "║");
        System.out.println("║  Price:   $" + padRight(req.getPrice(), 38) + "║");
        System.out.println("║                                                  ║");
        System.out.println("║  We look forward to seeing you!                  ║");
        System.out.println("║  — Trim                                          ║");
        System.out.println("╚══════════════════════════════════════════════════╝");

        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                BARBER NOTIFICATION               ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║ To:      " + padRight(req.getBarberEmail(), 40) + "║");
        System.out.println("║ Subject: New Appointment Booked                  ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Hi " + padRight(req.getBarberName() + ",", 45) + "║");
        System.out.println("║  A new appointment has been booked with you.     ║");
        System.out.println("║                                                  ║");
        System.out.println("║  Client:  " + padRight(req.getClientName(), 39) + "║");
        System.out.println("║  Service: " + padRight(req.getServiceType(), 39) + "║");
        System.out.println("║  Date:    " + padRight(req.getDate(), 39) + "║");
        System.out.println("║  Time:    " + padRight(req.getStartTime() + " – " + req.getEndTime(), 39) + "║");
        System.out.println("║  Appt ID: #" + padRight(req.getAppointmentId().toString(), 38) + "║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println("[NotificationService] " + notifId + " | 2 emails dispatched.");

        return ResponseEntity.ok(new NotificationResponse(
                notifId,
                "SENT",
                2,
                "Confirmation sent to " + req.getClientEmail()
                        + " and " + req.getBarberEmail()));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service is UP");
    }

    private String padRight(String s, int n) {
        if (s == null)
            s = "";
        if (s.length() > n)
            s = s.substring(0, n - 3) + "...";
        return String.format("%-" + n + "s", s);
    }
}