package com.solvd.userservice.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Document(collection = "users")
public class User implements Serializable {

    @Id
    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private Role role;
    private boolean isActivated;

    @RequiredArgsConstructor
    public enum Role {

        CLIENT("CLIENT"),
        EMPLOYEE("EMPLOYEE");

        private final String value;

    }

}
