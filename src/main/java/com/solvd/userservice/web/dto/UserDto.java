package com.solvd.userservice.web.dto;

import com.solvd.userservice.domain.User;
import lombok.Data;

@Data
public class UserDto {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private User.Role role;
    private boolean isActivated;

}
