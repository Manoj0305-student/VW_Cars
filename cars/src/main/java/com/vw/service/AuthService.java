package com.vw.service;

import com.vw.entities.Appointment;
import com.vw.entities.Customer;
import com.vw.entities.UserInfo;
import com.vw.repo.AppointmentRepo;
import com.vw.repo.CustomerRepo;
import com.vw.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    AppointmentRepo appointmentRepo;

    public Map<String, String> deleteUser(long id) {


        UserInfo userInfo = userRepository.findByUserId(id);
        Customer customer = customerRepo.findByName(userInfo.getUsername());
        List<Appointment> appointmentsList = customer.getAppointments();

        if (!appointmentsList.isEmpty()) {
            for (Appointment appointment : appointmentsList) {
                appointmentRepo.deleteById(appointment.getAppointmentId());
            }
        }

        customerRepo.deleteById(customer.getCustomerId());
        userRepository.deleteById(id);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("message", "Deleted id: " + id);
        return jsonMap;
    }
}
