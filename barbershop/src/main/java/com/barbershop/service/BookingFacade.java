package com.barbershop.service;

import com.barbershop.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;


@Component
public class BookingFacade {
    private static final int MAX_RETRIES = 3;
    private final Counter failedCounter;
    private final AppointmentService appointmentService;
    private static final Logger log = LoggerFactory.getLogger(BookingFacade.class);

    public BookingFacade(AppointmentService appointmentService, MeterRegistry registry) {
        this.appointmentService = appointmentService;
        this.failedCounter = registry.counter("failed_bookings");
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
                    log.error("Booking failed (non-lock error): {}", ex.getMessage());
                    if (ex instanceof RuntimeException)
                        throw (RuntimeException) ex;
                    throw new RuntimeException(ex);
                }

                if (attempt == MAX_RETRIES) {
                    failedCounter.increment();
                    log.error("Booking failed (non-lock error): {}", ex.getMessage());
                    throw new IllegalStateException(
                            "Slot is no longer available. Please select a different time.", ex);
                }
                try {
                    Thread.sleep(50L * attempt);
                    log.info("Retrying booking (attempt {}): {}", attempt, ex.getMessage());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new IllegalStateException("Booking failed after retries.");
    }
}