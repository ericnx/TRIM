package com.barbershop.controller;

import com.barbershop.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ENDPOINT 1: GET /
 *
 * Request flow:
 *   Browser
 *     → DispatcherServlet (Spring Front Controller)
 *     → HomeController.home()
 *     → AppointmentService.getAllServices()
 *     → BarberServiceRepository.findAll()
 *     → H2 (SELECT * FROM barber_service)
 *     ← List<BarberService> returned up the chain
 *     ← Thymeleaf renders home.html with the data
 *     ← HTTP 200 response sent to browser
 */
@Controller
public class HomeController {

    private final AppointmentService appointmentService;

    public HomeController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("services", appointmentService.getAllServices());
        return "home"; // resolves to templates/home.html
    }
}
