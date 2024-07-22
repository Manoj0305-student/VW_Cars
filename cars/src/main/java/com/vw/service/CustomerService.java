package com.vw.service;

import com.vw.dto.CustomerDto;
import com.vw.entities.Customer;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {


    public CustomerDto convertCustomerToDto(Customer customer) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setCustomerId(customer.getCustomerId());
        customerDto.setName(customer.getName());
        customerDto.setEmail(customer.getEmail());
        customerDto.setPhone(customer.getPhone());
        customerDto.setDlNumber(customer.getDlNumber());

        // Assuming executiveId is an integer field in CustomerDto
        if (customer.getExecutive()!= null) {
            customerDto.setExecutiveId(customer.getExecutive().getExecutiveId());
        }
        return customerDto;
    }
}
