package com.barbershop.repository;

import com.barbershop.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Long> {

    @Query("SELECT s FROM Slot s WHERE s.date = :date AND s.status = 'AVAILABLE'")
    List<Slot> findAvailableByDate(@Param("date") LocalDate date);
}