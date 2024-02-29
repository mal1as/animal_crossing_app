package com.itmo.coursework.dto;

import com.itmo.coursework.model.Role;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {

    private Long id;
    private String username;
    private String passwordHash;
    private List<Role> roles;
}
