package com.solvd.userservice.service;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.jwt.JwtToken;
import reactor.core.publisher.Mono;

public interface UserService {

    Mono<User> getById(String id);

    Mono<User> getByEmail(String email);

    Mono<User> update(User user);

    Mono<Void> updatePassword(String userId, String newPassword);

    Mono<Void> updatePassword(String userId, Password password);

    Mono<User> create(User user);

    Mono<Void> activate(JwtToken token);

    Mono<Void> delete(String id);

}
