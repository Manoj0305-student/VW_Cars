package com.vw.controller;

import com.vw.dto.AppointmentDto;
import com.vw.exceptions.AppointmentException;
import com.vw.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

//    // Create a new appointment
//    @PostMapping
//    public ResponseEntity<AppointmentDto> createAppointment(@RequestBody AppointmentDto appointmentDto) {
//        AppointmentDto createdAppointment = appointmentService.createAppointment(appointmentDto);
//        return ResponseEntity.ok(createdAppointment);
//    }

    // Create a new test drive appointment
    @PostMapping("/test-drive")
    public ResponseEntity<AppointmentDto> createTestDriveAppointment(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto createdAppointment = appointmentService.createTestDriveAppointment(appointmentDto);
        return ResponseEntity.ok(createdAppointment);
    }

    // Update an existing appointment
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable int id, @RequestBody AppointmentDto appointmentDto) {
        AppointmentDto updatedAppointment = appointmentService.updateAppointment(id, appointmentDto);
        return ResponseEntity.ok(updatedAppointment);
    }

    // Delete an appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteAppointment(@PathVariable int id) {
        appointmentService.deleteAppointment(id);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("message", "Executive is Deleted with id: "+id);
        return ResponseEntity.ok(jsonMap);
    }

    // Get all appointments
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        List<AppointmentDto> appointments = appointmentService.getAllAppointments();
        if(appointments.isEmpty()) {
            throw new AppointmentException("No Data Found!!");
        }
        return ResponseEntity.ok(appointments);
    }

    // Get an appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable int id) {
        AppointmentDto appointment = appointmentService.getAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }

    // Get appointments by executive ID
    @GetMapping("/executive/{executiveId}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByExecutive(@PathVariable int executiveId) {
        List<AppointmentDto> appointments = appointmentService.getAppointmentsByExecutive(executiveId);
        return ResponseEntity.ok(appointments);
    }

    // Book an appointment with a direct executive
    @PostMapping("/direct-executive")
    public ResponseEntity<AppointmentDto> bookAppointmentWithDirectExecutive(@RequestBody AppointmentDto appointmentDto) {
        AppointmentDto bookedAppointment = appointmentService.bookAppointmentWithDirectExecutive(appointmentDto);
        return ResponseEntity.ok(bookedAppointment);
    }

    // Buy a car
    @PostMapping("/buy-car/{appointmentId}")
    public ResponseEntity<Map<String,String>> buyACar(@PathVariable int appointmentId) {
        appointmentService.buyACar(appointmentId);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("message", "Dear Customer you SuccessFully Brought A car "+appointmentId);
        return ResponseEntity.ok(jsonMap);
    }
}
