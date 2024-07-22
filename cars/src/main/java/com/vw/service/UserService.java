package com.vw.service;


import com.vw.entities.Customer;
import com.vw.entities.Executive;
import com.vw.exceptions.ExecutiveException;
import com.vw.exceptions.ListOfCarIsEmptyException;
import com.vw.repo.CustomerRepo;
import com.vw.repo.ExecutiveRepo;
import com.vw.repo.UserRepository;
import com.vw.dto.UserInfoDTO;
import com.vw.entities.UserInfo;
import com.vw.entities.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerRepo customerRepository;
    @Autowired
    private ExecutiveRepo executiveRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass());



    public UserInfo saveUsers(UserInfoDTO user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<UserRole> roles = new HashSet<>();
        UserRole role = new UserRole(1, "USER");
        roles.add(role);
        UserInfo userInfo = UserInfo.builder().username(user.getUsername()).email(user.getEmail())
                .password(user.getPassword())
                .roles(roles).build();
        UserInfo createdUser = userRepository.save(userInfo);
        //saving the customer
        if (customerRepository.existsByName(user.getEmail()) && customerRepository.existsByName(user.getUsername())) {
            throw new ExecutiveException("Executive Resources already found with this Id: " + customerRepository.findByName(user.getUsername()));
        }

        Customer customer = Customer.builder().name(user.getUsername())
                .email(user.getEmail()).user(userInfo).build();
        customerRepository.save(customer);
        return createdUser;
    }

    public UserInfo saveAdmins(UserInfoDTO admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        Set<UserRole> roles = new HashSet<>();
        UserRole role = new UserRole(2, "ADMIN");
        roles.add(role);
        UserInfo userInfo = UserInfo.builder().username(admin.getUsername())
                .email(admin.getEmail())
                .password(admin.getPassword())
                .roles(roles).build();
        UserInfo createdUser = userRepository.save(userInfo);
        //creating executive
        if (executiveRepository.existsByEmail(admin.getEmail()) && executiveRepository.existsByName(admin.getUsername())) {
            throw new ExecutiveException("Executive Resources already found with this Id: " + executiveRepository.findByName(admin.getUsername()));
        }
        Executive executive = Executive.builder().name(admin.getUsername())
                .email(admin.getEmail()).user(userInfo).build();
        executiveRepository.save(executive);
        return createdUser;
    }

    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    public ResponseEntity<List<UserInfo>> fetchUser() {
         List<UserInfo> list = userRepository.findAll();
        if(list.isEmpty()) {
            throw new ListOfCarIsEmptyException("No Data Found!!");
        }
        return ResponseEntity.ok(list);
    }

    public UserInfo fetchUserbyId(long id) {
        return userRepository.findById(id).get();
    }

    public UserInfo fetchUserbyUsername(String name) {
        return userRepository.findByUsername(name).get();
    }
}
