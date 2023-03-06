package com.solvd.userservice.service;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.jwt.JwtToken;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    User update(User user);

    void updatePassword(Long userId, String newPassword);

    void updatePassword(Long userId, Password password);

    User create(User user);

    void activate(JwtToken token);

    void delete(Long id);

}
