package com.barbershop.controller;

import com.barbershop.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ENDPOINT 2: GET /appointments
 *
 * Request flow:
 *   Browser
 *     → DispatcherServlet (Spring Front Controller)
 *     → AppointmentController.listAppointments()
 *     → AppointmentService.getAllAppointments()
 *     → AppointmentRepository.findAll()
 *     → H2 (SELECT with JOINs across appointment, client, barber, service, schedule)
 *     ← List<AppointmentView> returned up the chain
 *     ← Thymeleaf renders appointments/list.html with the data
 *     ← HTTP 200 response sent to browser
 */
@Controller
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        model.addAttribute("appointments", appointmentService.getAllAppointments());
        return "appointments/list"; // resolves to templates/appointments/list.html
    }
}
