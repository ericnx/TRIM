package com.barbershop.service;

import com.barbershop.model.Slot;
import com.barbershop.repository.SlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SlotService {

    private final SlotRepository slotRepository;

    public SlotService(SlotRepository slotRepository) {
        this.slotRepository = slotRepository;
    }

    public List<Slot> getAvailableSlots(LocalDate date, Long barberId, Long serviceId) {
    List<Slot> slots = slotRepository.findAvailableByDate(date);

    if (barberId != null) {
        slots = slots.stream()
            .filter(slot -> slot.getSchedule().getStaff().getPersonId().equals(barberId))
            .collect(Collectors.toList());
    }
    return slots;
}
}