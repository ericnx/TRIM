package com.barbershop.service;

import com.barbershop.model.*;
import com.barbershop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final SlotRepository slotRepo;
    private final BarberServiceRepository serviceRepo;
    private final BarberRepository barberRepo;
    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    public AppointmentService(AppointmentRepository appointmentRepo,
            SlotRepository slotRepo,
            BarberServiceRepository serviceRepo,
            BarberRepository barberRepo) {
        this.appointmentRepo = appointmentRepo;
        this.slotRepo = slotRepo;
        this.serviceRepo = serviceRepo;
        this.barberRepo = barberRepo;
    }

    @Transactional(readOnly = true)
    public List<AppointmentView> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<BarberService> getAllServices() {
        return (List<BarberService>) serviceRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Barber> getAllBarbers() {
        return barberRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Slot> getAvailableSlots(LocalDate date) {
        return slotRepo.findAvailableByDate(date);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public Appointment bookAppointment(Long clientId, Long serviceId, Long scheduleId, LocalTime startTime,
            LocalDate date) {

        boolean success = slotRepo.reserveSlot(scheduleId, date, startTime);

        if (!success) {
            log.warn("Double-booking prevented for Client {} at {} on {}", clientId, startTime, date);
            throw new IllegalStateException("Sorry, this slot was just taken by another user.");
        }

        BarberService service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        Appointment appointment = new Appointment();
        appointment.setClientId(clientId);
        appointment.setServiceId(serviceId);
        appointment.setScheduleId(scheduleId);
        appointment.setSlotStartTime(startTime);
        appointment.setDate(date);
        appointment.setStatus(Appointment.Status.PENDING);
        appointment.setCurrentPrice(service.getPrice());

        log.info("Booking confirmed for Client ID: {} for {}", clientId, startTime);
        return appointmentRepo.save(appointment);
    }
}