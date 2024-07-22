package com.vw.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vw.entities.Appointment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDto {
	
	private int id;
	private String brand;
	private String name;
	private String fuel;
	private String transmission;
	private String type;
	private String color;
	private Date model;
	private double price;
	private String description;
	private String content;

	private List<Appointment> appointmentDtoList;
}
