package com.vw.repo;

import com.vw.entities.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserInfo, Long> {

     Optional<UserInfo> findByUsername(String username);

     UserInfo findByUserId(long userId);


}
