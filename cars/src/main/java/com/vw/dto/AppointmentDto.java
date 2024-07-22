package com.vw.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import java.util.Date;

@Data
public class AppointmentDto {

    private int appointmentId;
    private Date appointmentDate;
    private String appointmentType;
    private int carId;
    private int executiveId;
    private int customerId;
    private boolean approved;
    @JsonIgnore
    private CustomerDto customer;
}
