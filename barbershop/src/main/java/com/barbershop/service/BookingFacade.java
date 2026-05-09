package com.barbershop.service;

import com.barbershop.model.Appointment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class BookingFacade {
    private final Counter failedCounter;
    private final AppointmentService appointmentService;
    private static final Logger log = LoggerFactory.getLogger(BookingFacade.class);

    public BookingFacade(AppointmentService appointmentService, MeterRegistry registry) {
        this.appointmentService = appointmentService;
        this.failedCounter = registry.counter("failed_bookings");
    }

    @Transactional
    public Appointment bookWithRetry(Long clientId, Long serviceId, Long scheduleId, LocalTime startTime, LocalDate date) {
        try {
            return appointmentService.bookAppointment(clientId, serviceId, scheduleId, startTime, date);
        } catch (Exception ex) {
            failedCounter.increment();
            log.error("Booking failed for Client ID {}: {}", clientId, ex.getMessage());
            
            throw ex; 
        }
    }
}