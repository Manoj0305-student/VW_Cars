package com.vw.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserInfoDTO {
    private String username;
    private String password;
    private String email;
}
