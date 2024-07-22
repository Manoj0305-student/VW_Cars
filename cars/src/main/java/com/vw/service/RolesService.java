package com.vw.service;

import com.vw.entities.UserRole;
import com.vw.exceptions.ListOfCarIsEmptyException;
import com.vw.repo.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepository;

    public ResponseEntity<List<UserRole>> showRoles() {
        List<UserRole> list = rolesRepository.findAll();
        if (list.isEmpty()) {
            throw new ListOfCarIsEmptyException("No Data Found!!");
        }
        return ResponseEntity.ok(list);
    }
    public UserRole saveRoles(UserRole role){
        return rolesRepository.save(role);
    }
}
