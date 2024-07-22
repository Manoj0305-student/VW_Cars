package com.vw.repo;

import com.vw.entities.Appointment;
import com.vw.entities.Car;
import com.vw.entities.Executive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Integer> {

    List<Appointment> findByCarAndAppointmentDate(Car car, Date appointmentDate);

    List<Appointment> findByExecutive(Executive executive);
}
