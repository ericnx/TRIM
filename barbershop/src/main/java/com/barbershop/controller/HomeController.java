package com.barbershop.controller;

import com.barbershop.service.AppointmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final AppointmentService appointmentService;

    public HomeController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("services", appointmentService.getAllServices());
        return "home";
    }
}
