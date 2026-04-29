package com.barbershop.service;

import com.barbershop.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class BookingFacade {
    private static final int MAX_RETRIES = 3;
    private final AppointmentService appointmentService;

    public BookingFacade(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Appointment bookWithRetry(Long clientId, Long serviceId, Long slotId) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return appointmentService.bookAppointment(clientId, serviceId, slotId);

            } catch (Exception ex) {
                Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
                boolean isLockConflict = cause.getClass().getSimpleName().contains("OptimisticLock") ||
                        ex.getClass().getSimpleName().contains("OptimisticLock");

                if (!isLockConflict) {
                    if (ex instanceof RuntimeException)
                        throw (RuntimeException) ex;
                    throw new RuntimeException(ex);
                }

                if (attempt == MAX_RETRIES) {
                    throw new IllegalStateException(
                            "Slot is no longer available. Please select a different time.", ex);
                }
                try {
                    Thread.sleep(50L * attempt);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new IllegalStateException("Booking failed after retries.");
    }
}