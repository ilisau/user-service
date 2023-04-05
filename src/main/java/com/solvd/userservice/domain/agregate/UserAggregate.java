package com.solvd.userservice.domain.agregate;

import com.solvd.userservice.domain.User;
import lombok.Data;

@Data
public class UserAggregate {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private User.Role role;
    private boolean isActivated;

}
