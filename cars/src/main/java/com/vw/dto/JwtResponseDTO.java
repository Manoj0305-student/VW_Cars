package com.vw.dto;

import com.vw.entities.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDTO {
    private long id;
    private String username;
    private String email;
    private String accessToken;
    private Set<UserRole> userRoles;


}