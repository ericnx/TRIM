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
    private final ClientRepository clientRepo;
    private final BarberServiceRepository serviceRepo;
    private final BarberRepository barberRepo;
    private static final Logger log = LoggerFactory.getLogger(AppointmentService.class);

    public AppointmentService(AppointmentRepository appointmentRepo,
            SlotRepository slotRepo,
            ClientRepository clientRepo,
            BarberServiceRepository serviceRepo,
            BarberRepository barberRepo) {
        this.appointmentRepo = appointmentRepo;
        this.slotRepo = slotRepo;
        this.clientRepo = clientRepo;
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
    public Appointment bookAppointment(Long clientId, Long serviceId, Long scheduleId, LocalTime startTime, LocalDate date) {

        SlotId slotId = new SlotId(scheduleId, startTime, date);
        
        Slot slot = slotRepo.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found"));

        if (slot.getStatus() != Slot.Status.AVAILABLE) {
            throw new IllegalStateException("Slot is no longer available.");
        }

        slot.setStatus(Slot.Status.BOOKED);
        slotRepo.save(slot);

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        BarberService service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));

        return appointmentRepo.save(
                new Appointment(client, slot, service, Appointment.Status.PENDING, service.getPrice()));
    }
}