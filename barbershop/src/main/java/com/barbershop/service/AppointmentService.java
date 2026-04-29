package com.barbershop.service;

import com.barbershop.model.Appointment;
import com.barbershop.model.AppointmentView;
import com.barbershop.model.Barber;
import com.barbershop.model.BarberService;
import com.barbershop.model.Client;
import com.barbershop.model.Slot;
import com.barbershop.repository.AppointmentRepository;
import com.barbershop.repository.BarberRepository;
import com.barbershop.repository.BarberServiceRepository;
import com.barbershop.repository.ClientRepository;
import com.barbershop.repository.ScheduleRepository;
import com.barbershop.repository.SlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final SlotRepository slotRepo;
    private final ClientRepository clientRepo;
    private final BarberServiceRepository serviceRepo;
    private final BarberRepository barberRepo;

    public AppointmentService(AppointmentRepository appointmentRepo,
            SlotRepository slotRepo,
            ClientRepository clientRepo,
            BarberServiceRepository serviceRepo,
            ScheduleRepository scheduleRepo,
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
    public Appointment bookAppointment(Long clientId, Long serviceId, Long slotId) {

        Slot slot = slotRepo.findById(slotId)
                .orElseThrow(() -> new IllegalArgumentException("Slot not found: " + slotId));

        if (slot.getStatus() != Slot.Status.AVAILABLE) {
            throw new IllegalStateException("Slot is no longer available.");
        }

        slot.setStatus(Slot.Status.BOOKED);
        slotRepo.save(slot);

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + clientId));

        BarberService service = serviceRepo.findById(serviceId)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + serviceId));

        return appointmentRepo.save(
                new Appointment(client, service, slot.getSchedule(),
                        slot, Appointment.Status.PENDING, service.getPrice()));
    }
}