package com.vw.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Executive {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "executive_id")
    private int executiveId;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Enter a Valid email!")
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "executive")
    @JsonManagedReference(value = "executiveAppointments")
    private List<Appointment> appointments = new ArrayList<>();

    @OneToMany(mappedBy = "executive")
    @JsonManagedReference(value ="executiveCustomers")
    private List<Customer> customers = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserInfo user;
}
