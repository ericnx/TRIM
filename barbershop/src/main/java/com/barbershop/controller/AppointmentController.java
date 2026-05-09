package com.barbershop.controller;

import com.barbershop.model.Appointment;
import com.barbershop.model.Slot;
import com.barbershop.repository.BarberRepository;
import com.barbershop.repository.BarberServiceRepository;
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

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final BarberRepository barberRepository;
    private final BarberServiceRepository serviceRepository;
    private final SlotService slotService;
    private final AppointmentService appointmentService;
    private final BookingFacade bookingFacade;

    public AppointmentController(BarberRepository barberRepository,
            BarberServiceRepository serviceRepository,
            SlotService slotService,
            AppointmentService appointmentService,
            BookingFacade bookingFacade) {
        this.barberRepository = barberRepository;
        this.serviceRepository = serviceRepository;
        this.slotService = slotService;
        this.appointmentService = appointmentService;
        this.bookingFacade = bookingFacade;
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
                        return s.getSchedule().getStaff().getPersonId().equals(barberId);
                    })
                    .toList();
        }

        // service filter
        if (serviceType != null && !serviceType.isEmpty()) {
            availableSlots = availableSlots.stream()
                    .filter(s -> s.getSchedule().getStaff().getServices().stream()
                            .anyMatch(svc -> svc.getType().equalsIgnoreCase(serviceType)))
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
        List<java.math.BigDecimal> prices = availableSlots.stream()
                .flatMap(s -> s.getSchedule().getStaff().getServices().stream())
                .filter(svc -> serviceType == null || serviceType.isEmpty()
                        || svc.getType().equalsIgnoreCase(serviceType))
                .map(com.barbershop.model.BarberService::getPrice)
                .distinct()
                .toList();
        model.addAttribute("prices", prices);

        return "appointments/slots";
    }

    @GetMapping("/book")
    public String bookForm(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        LocalDate selectedDate = (date == null) ? LocalDate.now() : date;

        model.addAttribute("services", appointmentService.getAllServices());
        model.addAttribute("availableSlots", appointmentService.getAvailableSlots(selectedDate));
        model.addAttribute("selectedDate", selectedDate);

        return "appointments/book";
    }

    @PostMapping("/book")
    public String book(@RequestParam Long clientId,
            @RequestParam Long serviceId,
            @RequestParam Long scheduleId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            RedirectAttributes ra) {
        try {
            Appointment appt = bookingFacade.bookWithRetry(clientId, serviceId, scheduleId, startTime, date);
            ra.addFlashAttribute("appointment", appt);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/appointments/book?date=" + date;
        }
        return "redirect:/appointments/confirm";
    }

    @GetMapping("/confirm")
    public String confirm() {
        return "appointments/confirm";
    }
}