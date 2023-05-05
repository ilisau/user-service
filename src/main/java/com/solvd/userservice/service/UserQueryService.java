package com.solvd.userservice.service;

import com.solvd.userservice.domain.User;
import reactor.core.publisher.Mono;

public interface UserQueryService {

    /**
     * Get user by id.
     *
     * @param id id
     * @return user
     */
    Mono<User> getById(String id);

    /**
     * Get user by email.
     *
     * @param email email
     * @return user
     */
    Mono<User> getByEmail(String email);

}
