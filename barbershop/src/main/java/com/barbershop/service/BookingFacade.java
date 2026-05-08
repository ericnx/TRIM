package com.barbershop.service;

import com.barbershop.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.LocalDate;
import java.time.LocalTime;

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

    public Appointment bookWithRetry(Long clientId, Long serviceId, Long scheduleId, LocalTime startTime, LocalDate date) {
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                return appointmentService.bookAppointment(clientId, serviceId, scheduleId, startTime, date);

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
                    log.error("Booking failed after maximum retries: {}", ex.getMessage());
                    throw new IllegalStateException(
                            "The slot was contested by another booking. Please try selecting the slot again.", ex);
                }
                try {
                    Thread.sleep(50L * attempt);
                    log.info("Retrying booking due to conflict (attempt {}): {}", attempt, ex.getMessage());
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        throw new IllegalStateException("Booking failed after retries.");
    }
}