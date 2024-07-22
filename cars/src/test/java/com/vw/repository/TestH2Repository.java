package com.vw.repository;

import com.vw.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestH2Repository extends JpaRepository<Car, Integer> {
}
