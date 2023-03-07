package com.solvd.userservice.web.dto;

import lombok.Data;

@Data
public class PasswordDto {

    private String oldPassword;
    private String newPassword;

}
