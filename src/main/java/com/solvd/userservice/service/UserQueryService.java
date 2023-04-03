package com.solvd.userservice.service;

import com.solvd.userservice.domain.User;
import reactor.core.publisher.Mono;

public interface UserQueryService {

    Mono<User> getById(String id);

    Mono<User> getByEmail(String email);

}
