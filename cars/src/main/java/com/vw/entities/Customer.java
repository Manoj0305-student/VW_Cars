package com.vw.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

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

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;
    private String name;

    @Email(message = "Enter valid email!",regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~\\.-]+@[a-zA-Z0-9.-]+$")
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    @Pattern(regexp = "^\\d{2}-\\d{10}$")
    private String phone;


    @Column(unique = true)
    @Pattern(regexp = "^(([A-Z]{2}[0-9]{2})( )|([A-Z]{2}-[0-9]{2}))((19|20)[0-9][0-9])[0-9]{7}$")
    private String dlNumber;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference(value ="customerAppointments")
    private List<Appointment> appointments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "executiveId")
    @JsonBackReference(value ="executiveCustomers")
    private Executive executive;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    private UserInfo user;


//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
//    @JsonIgnore
//    private List<Car> cars;
}
