package com.vw.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.util.List;

@Data
public class CustomerDto {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String dlNumber;
    private int executiveId;

}
