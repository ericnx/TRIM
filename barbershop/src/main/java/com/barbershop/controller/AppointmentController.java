package com.barbershop.controller;

import com.barbershop.model.Client;
import com.barbershop.model.Slot;
import com.barbershop.repository.BarberRepository;
import com.barbershop.repository.BarberServiceRepository;
import com.barbershop.repository.ScheduleRepository;
import com.barbershop.repository.ClientRepository;
import com.barbershop.service.AppointmentService;
import com.barbershop.service.SlotService;
import com.barbershop.service.BookingFacade;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final BarberRepository barberRepository;
    private final BarberServiceRepository serviceRepository;
    private final SlotService slotService;
    private final AppointmentService appointmentService;
    private final BookingFacade bookingFacade;
    private final ClientRepository clientRepository;

    public AppointmentController(BarberRepository barberRepository,
            BarberServiceRepository serviceRepository,
            SlotService slotService,
            AppointmentService appointmentService,
            BookingFacade bookingFacade,
            ScheduleRepository scheduleRepository,
            ClientRepository clientRepository) {
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
        this.slotService = slotService;
        this.appointmentService = appointmentService;
        this.bookingFacade = bookingFacade;
        this.clientRepository = clientRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments/list";
    }

    @GetMapping("/slots")
    public String showSlots(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) Long barberId,
            @RequestParam(required = false) String serviceType,
            @RequestParam(required = false) String timeOfDay,
            Model model) {

        LocalDate selectedDate = (date == null) ? LocalDate.now() : date;

        LocalDate firstDayOfMonth = selectedDate.withDayOfMonth(1);
        int leadingEmptyDays = firstDayOfMonth.getDayOfWeek().getValue() % 7;
        List<LocalDate> daysInMonth = new ArrayList<>();
        for (int i = 0; i < selectedDate.lengthOfMonth(); i++) {
            daysInMonth.add(firstDayOfMonth.plusDays(i));
        }

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("leadingEmptyDays", leadingEmptyDays);

        List<com.barbershop.model.Barber> allBarbers = new ArrayList<>();
        barberRepository.findAll().forEach(allBarbers::add);
        model.addAttribute("allBarbers", allBarbers);

        List<String> allServiceTypes = new ArrayList<>();
        serviceRepository.findAll().forEach(s -> {
            if (!allServiceTypes.contains(s.getType())) {
                allServiceTypes.add(s.getType());
            }
        });
        model.addAttribute("allServiceTypes", allServiceTypes);

        model.addAttribute("selectedBarberId", barberId);
        model.addAttribute("selectedServiceType", serviceType);
        model.addAttribute("selectedTimeOfDay", timeOfDay);

        List<Slot> availableSlots = slotService.getAvailableSlots(selectedDate, null, null);

        // barber filter
        if (barberId != null) {
            availableSlots = availableSlots.stream()
                    .filter(s -> {
                        return s.getScheduleId().equals(barberId);
                    })
                    .toList();
        }
        model.addAttribute("slots", availableSlots);

        // service filter
        if (serviceType != null && !serviceType.isEmpty()) {
            List<Long> qualifiedBarberIds = StreamSupport.stream(serviceRepository.findAll().spliterator(), false)
                    .filter(svc -> svc.getType().equalsIgnoreCase(serviceType))
                    .map(svc -> svc.getBarberId())
                    .toList();

            availableSlots = availableSlots.stream()
                    .filter(s -> qualifiedBarberIds.contains(s.getScheduleId()))
                    .toList();
        }

        // time filter
        if (timeOfDay != null && !timeOfDay.isEmpty()) {
            availableSlots = availableSlots.stream()
                    .filter(s -> {
                        int hour = s.getSlotStartTime().getHour();
                        return switch (timeOfDay.toLowerCase()) {
                            case "morning" -> hour < 12;
                            case "afternoon" -> hour >= 12 && hour < 17;
                            case "evening" -> hour >= 17;
                            default -> true;
                        };
                    })
                    .toList();
        }

        model.addAttribute("slots", availableSlots);

        // prices
        if (serviceType != null && !serviceType.isEmpty()) {
            var service = StreamSupport.stream(serviceRepository.findAll().spliterator(), false)
                    .filter(s -> s.getType().equalsIgnoreCase(serviceType))
                    .findFirst()
                    .orElse(null);

            if (service != null) {
                model.addAttribute("prices", List.of(service.getPrice()));
            }
        }
        return "appointments/slots";
    }

    @GetMapping("/book")
    public String showBookForm(@RequestParam Long serviceId,
            @RequestParam Long scheduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            Model model) {

        var barber = barberRepository.findById(scheduleId).orElseThrow();
        var service = serviceRepository.findById(serviceId).orElseThrow();

        model.addAttribute("service", service);
        model.addAttribute("date", date);
        model.addAttribute("startTime", startTime);
        model.addAttribute("scheduleId", scheduleId);
        model.addAttribute("barber", barber);

        return "appointments/book";
    }

    @PostMapping("/book")
    public String processBooking(@RequestParam Long serviceId,
            @RequestParam Long scheduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String clientFirstName,
            @RequestParam String clientLastName,
            @RequestParam String clientEmail,
            @RequestParam String clientPhone,
            RedirectAttributes ra) {
        try {
            Client client = clientRepository.findByEmail(clientEmail)
                    .orElseGet(() -> {
                        var newClient = new com.barbershop.model.Client(
                                clientFirstName,
                                clientLastName,
                                clientPhone,
                                clientEmail);
                        return clientRepository.saveAndFlush(newClient);
                    });

            bookingFacade.bookWithRetry(client.getClientId(), serviceId, scheduleId, startTime, date);

            return "redirect:/appointments/confirm";
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Failed: " + e.getMessage());
            return "redirect:/appointments/slots?date=" + date;
        }
    }

    @GetMapping("/confirm")
    public String showSuccessPage() {
        return "appointments/confirm";
    }
}