package com.solvd.userservice.service;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.jwt.JwtToken;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> getById(Long id);

    Mono<User> getByEmail(String email);

    Mono<User> update(User user);

    void updatePassword(Long userId, String newPassword);

    void updatePassword(Long userId, Password password);

    Mono<User> create(User user);

    void activate(JwtToken token);

    void delete(Long id);

}
