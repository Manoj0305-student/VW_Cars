package com.vw.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "cars")
@NoArgsConstructor
@AllArgsConstructor
public class Car {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int carId;
	private String name;
	private String brand;
	private String color;
	private String fuel;
	private String type;
	private String transmission;
	private Date model;
	private String description;
	private String imgName;
	private String contentType;
	private double price;

	
	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] imgData;

	@JsonManagedReference(value ="carAppointments")
    @OneToMany(mappedBy = "car",cascade = CascadeType.ALL)
	private List<Appointment> appointmentList;


//	@ManyToOne
//	@JoinColumn(name = "customer_id")
//	private Customer customer;
}
