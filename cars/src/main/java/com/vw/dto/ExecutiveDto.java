package com.vw.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ExecutiveDto {

    private int executiveId;

    private String name;

    private String email;

}

