package com.solvd.userservice.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.web.dto.validation.OnCreate;
import com.solvd.userservice.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class UserDto {

    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "firstName must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "firstName must be less than 255 characters", groups = {OnCreate.class, OnUpdate.class})
    private String firstName;

    @Length(max = 255, message = "lastName must be less than 255 characters", groups = {OnCreate.class, OnUpdate.class})
    private String lastName;

    @Email
    @NotNull(message = "email must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "email must be less than 255 characters", groups = {OnCreate.class, OnUpdate.class})
    private String email;

    @NotNull(message = "password must be not null", groups = {OnCreate.class, OnUpdate.class})
    @NotEmpty(message = "password must be not empty", groups = {OnCreate.class, OnUpdate.class})
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Length(max = 255, message = "phoneNumber must be less than 255 characters", groups = {OnCreate.class, OnUpdate.class})
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private User.Role role;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updatedAt;

}
