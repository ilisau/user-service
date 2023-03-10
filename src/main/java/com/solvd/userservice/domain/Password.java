package com.solvd.userservice.domain;

import lombok.Data;

@Data
public class Password {

    private String oldPassword;
    private String newPassword;

}
