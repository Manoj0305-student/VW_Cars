package com.vw.repo;

import com.vw.entities.Executive;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExecutiveRepo extends JpaRepository<Executive,Integer> {
     boolean existsByEmail(String email);
     boolean existsByName(String name);
     Executive findByName(String name);

}
