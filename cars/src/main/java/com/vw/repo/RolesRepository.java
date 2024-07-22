package com.vw.repo;

import com.vw.entities.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<UserRole, Long> {
}
