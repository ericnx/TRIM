package com.barbershop.controller;

import com.barbershop.model.Appointment;
import com.barbershop.service.AppointmentService;
import com.barbershop.service.BookingFacade;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final BookingFacade bookingFacade;

    public AppointmentController(AppointmentService appointmentService,
            BookingFacade bookingFacade) {
        this.appointmentService = appointmentService;
        this.bookingFacade = bookingFacade;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments/list";
    }

    @GetMapping("/slots")
    public String slots(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        if (date == null)
            date = LocalDate.of(2026, 3, 16);
        model.addAttribute("slots", appointmentService.getAvailableSlots(date));
        model.addAttribute("selectedDate", date);
        return "appointments/slots";
    }

    @GetMapping("/book")
    public String bookForm(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        if (date == null)
            date = LocalDate.of(2026, 3, 16);
        model.addAttribute("services", appointmentService.getAllServices());
        model.addAttribute("availableSlots", appointmentService.getAvailableSlots(date));
        model.addAttribute("selectedDate", date);
        return "appointments/book";
    }

    @PostMapping("/book")
    public String book(@RequestParam Long clientId,
            @RequestParam Long serviceId,
            @RequestParam Long slotId,
            RedirectAttributes ra) {
        try {
            Appointment appt = bookingFacade.bookWithRetry(clientId, serviceId, slotId);
            ra.addFlashAttribute("appointment", appt);
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/appointments/book";
        }
        return "redirect:/appointments/confirm";
    }

    @GetMapping("/confirm")
    public String confirm() {
        return "appointments/confirm";
    }
}