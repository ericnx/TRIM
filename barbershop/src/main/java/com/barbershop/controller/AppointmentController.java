package com.barbershop.controller;

import com.barbershop.dto.SlotDTO;
import com.barbershop.model.*;
import com.barbershop.repository.*;
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
    private final ClientRepository clientRepository;
    private final ScheduleRepository scheduleRepository;

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
        this.scheduleRepository = scheduleRepository;
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

        List<Barber> allBarbers = barberRepository.findAll();
        List<BarberService> allServices = serviceRepository.findAll();
        List<Schedule> allSchedules = scheduleRepository.findAll();
        List<Slot> rawSlots = slotService.getAvailableSlots(selectedDate, null, null);
        
        List<SlotDTO> displaySlots = new ArrayList<>();

        for (Slot slot : rawSlots) {
            Schedule sch = allSchedules.stream()
                    .filter(s -> s.getScheduleId().equals(slot.getScheduleId()))
                    .findFirst().orElse(null);

            if (sch == null) continue;

            Barber barber = allBarbers.stream()
                    .filter(b -> b.getBarberId().equals(sch.getStaffId()))
                    .findFirst().orElse(null);

            if (barber == null) continue;

            allServices.stream()
                    .filter(svc -> svc.getBarberId().equals(barber.getBarberId()))
                    .filter(svc -> serviceType == null || serviceType.isEmpty() || svc.getType().equalsIgnoreCase(serviceType))
                    .forEach(svc -> {
                        SlotDTO dto = new SlotDTO();
                        dto.setScheduleId(slot.getScheduleId());
                        dto.setServiceId(svc.getServiceId());
                        dto.setStartTime(slot.getSlotStartTime());
                        dto.setDate(slot.getDate());
                        dto.setBarberName(barber.getFirstName() + " " + barber.getLastName());
                        dto.setServiceType(svc.getType());
                        dto.setPrice(svc.getPrice());
                        dto.setDuration(svc.getDuration());
                        displaySlots.add(dto);
                    });
        }

        List<SlotDTO> finalSlots = displaySlots.stream()
                .filter(dto -> barberId == null || barberId == 0 || allSchedules.stream()
                        .anyMatch(s -> s.getScheduleId().equals(dto.getScheduleId()) && s.getStaffId().equals(barberId)))
                .filter(dto -> {
                    if (timeOfDay == null || timeOfDay.isEmpty()) return true;
                    int hour = dto.getStartTime().getHour();
                    return switch (timeOfDay.toLowerCase()) {
                        case "morning" -> hour < 12;
                        case "afternoon" -> hour >= 12 && hour < 17;
                        case "evening" -> hour >= 17;
                        default -> true;
                    };
                }).toList();

        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("daysInMonth", daysInMonth);
        model.addAttribute("leadingEmptyDays", leadingEmptyDays);
        model.addAttribute("allBarbers", allBarbers);
        model.addAttribute("allServiceTypes", allServices.stream().map(BarberService::getType).distinct().toList());
        
        model.addAttribute("selectedBarberId", barberId);
        model.addAttribute("selectedServiceType", serviceType);
        model.addAttribute("selectedTimeOfDay", timeOfDay);
        
        model.addAttribute("slots", finalSlots);

        return "appointments/slots";
    }

    @GetMapping("/book")
    public String showBookForm(@RequestParam Long serviceId, 
                               @RequestParam Long scheduleId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime, 
                               Model model) {
        
        model.addAttribute("service", serviceRepository.findById(serviceId).orElseThrow());
        // For the barber lookup, we look at the schedule table first
        Schedule sch = scheduleRepository.findAll().stream()
                .filter(s -> s.getScheduleId().equals(scheduleId))
                .findFirst().orElseThrow();
        
        model.addAttribute("barber", barberRepository.findById(sch.getStaffId()).orElseThrow());
        model.addAttribute("date", date);
        model.addAttribute("startTime", startTime);
        model.addAttribute("scheduleId", scheduleId);
        return "appointments/book";
    }

    @PostMapping("/book")
    public String processBooking(@RequestParam Long serviceId, 
                                 @RequestParam Long scheduleId,
                                 @RequestParam LocalDate date, 
                                 @RequestParam LocalTime startTime,
                                 @RequestParam String clientFirstName, 
                                 @RequestParam String clientLastName,
                                 @RequestParam String clientEmail, 
                                 @RequestParam String clientPhone, 
                                 RedirectAttributes ra) {
        try {
            Client client = clientRepository.findByEmail(clientEmail).orElseGet(() -> 
                clientRepository.saveAndFlush(new Client(clientFirstName, clientLastName, clientPhone, clientEmail)));
            
            bookingFacade.bookWithRetry(client.getClientId(), serviceId, scheduleId, startTime, date);
            return "redirect:/appointments/confirm";
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Booking failed: " + e.getMessage());
            return "redirect:/appointments/slots?date=" + date;
        }
    }

    @GetMapping("/confirm")
    public String showSuccessPage() {
        return "appointments/confirm";
    }
}