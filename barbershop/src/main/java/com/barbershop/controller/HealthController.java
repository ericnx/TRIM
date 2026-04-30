package com.barbershop.controller;

import com.barbershop.repository.SlotRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {
    private static final Logger log = LoggerFactory.getLogger(HealthController.class);
    private static final String NOTIF_HEALTH_URL = "http://localhost:8080/api/notifications/health";
    private final SlotRepository slotRepository;
    private final RestTemplate restTemplate;

    public HealthController(SlotRepository slotRepository,
            RestTemplate restTemplate) {
        this.slotRepository = slotRepository;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        log.info("Health check requested.");
        Map<String, Object> result = new LinkedHashMap<>();
        boolean allUp = true;
        try {
            slotRepository.count();
            result.put("database", "UP");
        } catch (Exception e) {
            result.put("database", "DOWN");
            log.error("Health check: database DOWN. {}", e.getMessage());
            allUp = false;
        }
        try {
            restTemplate.getForObject(NOTIF_HEALTH_URL, String.class);
            result.put("notifications", "UP");
        } catch (Exception e) {
            result.put("notifications", "UNREACHABLE");
            log.warn("Health check: notification service unreachable.");
        }
        result.put("app", "UP");
        result.put("status", allUp ? "UP" : "DOWN");
        result.put("timestamp", LocalDateTime.now().toString());
        HttpStatus status = allUp ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return ResponseEntity.status(status).body(result);
    }
}