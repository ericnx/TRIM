package com.barbershop.repository;

import com.barbershop.model.BarberService;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarberServiceRepository extends CrudRepository<BarberService, Long> {}
