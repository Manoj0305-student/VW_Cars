package com.vw.controller;

import com.vw.dto.CustomerDto;
import com.vw.dto.ExecutiveDto;
import com.vw.exceptions.CustomerException;
import com.vw.exceptions.ExecutiveException;
import com.vw.service.ExecutiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/executives")
public class ExecutiveController {

    @Autowired
    private ExecutiveService executiveService;

    // Create a new customer
    @PostMapping("/customers")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {
        CustomerDto createdCustomer = executiveService.createCustomer(customerDto);
        return ResponseEntity.ok(createdCustomer);
    }

    // Update an existing customer
    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable int id, @RequestBody CustomerDto customerDto) {
        CustomerDto updatedCustomer = executiveService.updateCustomer(id, customerDto);
        return ResponseEntity.ok(updatedCustomer);
    }

    // Assign a customer to an executive
    @PutMapping("/{executiveId}/customers/{customerId}")
    public ResponseEntity<Map<String,String>> assignCustomerToExecutive(@PathVariable int executiveId, @PathVariable int customerId) {
        executiveService.assignCustomerToExecutive(executiveId, customerId);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("message", "Assigned successfully with  executiveId: "+executiveId);
        return ResponseEntity.ok(jsonMap);
    }

    // Get all customers assigned to an executive
    @GetMapping("/{executiveId}/customers")
    public ResponseEntity<List<CustomerDto>> getCustomersByExecutive(@PathVariable int executiveId) {
        List<CustomerDto> customers = executiveService.getCustomersByExecutive(executiveId);
        return ResponseEntity.ok(customers);
    }

    // Get customer by ID
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDto> getCustomerById(@PathVariable int id) {
        CustomerDto customer = executiveService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    // Get all customers
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = executiveService.getAllCustomers();
        if(customers.isEmpty()) {
            throw new CustomerException("No Data Found!!");
        }
        return ResponseEntity.ok(customers);
    }

    // Delete a customer
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Map<String,String>> deleteCustomer(@PathVariable int id) {
        executiveService.deleteCustomer(id);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("message", "Customer is Deleted with id: "+id);
        return ResponseEntity.ok(jsonMap);
    }

    @PostMapping
    public ResponseEntity<ExecutiveDto> createExecutive(@RequestBody ExecutiveDto executiveDto) {
        ExecutiveDto createdExecutive = executiveService.createExecutive(executiveDto);
        return ResponseEntity.ok(createdExecutive);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExecutiveDto> updateExecutive(@PathVariable int id, @RequestBody ExecutiveDto executiveDto) {
        ExecutiveDto updatedExecutive = executiveService.updateExecutive(id, executiveDto);
        return ResponseEntity.ok(updatedExecutive);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExecutiveDto> getExecutiveById(@PathVariable int id) {
        ExecutiveDto executive = executiveService.getExecutiveById(id);
        return ResponseEntity.ok(executive);
    }

    @GetMapping
    public ResponseEntity<List<ExecutiveDto>> getAllExecutives() {
        List<ExecutiveDto> executives = executiveService.getAllExecutives();
        if(executives.isEmpty()){
            throw new ExecutiveException("Executives Not Found");
        }
        return ResponseEntity.ok(executives);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteExecutive(@PathVariable int id) {
        executiveService.deleteExecutive(id);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("message", "Executive is Deleted with id: "+id);
        return ResponseEntity.ok(jsonMap);
    }
}
