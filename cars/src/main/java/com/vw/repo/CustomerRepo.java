package com.vw.repo;

import com.vw.entities.Customer;
import com.vw.entities.Executive;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRepo extends JpaRepository<Customer,Integer> {

    List<Customer> findByExecutive(Executive executive);

     boolean existsByName(String name);
     Customer findByName(String name);



}
