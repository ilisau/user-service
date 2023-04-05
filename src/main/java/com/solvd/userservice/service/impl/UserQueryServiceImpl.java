package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "users", key = "#id")
    public Mono<User> getById(String id) {
        Mono<User> error = Mono.error(new UserNotFoundException("User with id " + id + " not found"));
        return userRepository.findById(id)
                .switchIfEmpty(error);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public Mono<User> getByEmail(String email) {
        Mono<User> error = Mono.error(new UserNotFoundException("User with email " + email + " not found"));
        return userRepository.findByEmail(email)
                .switchIfEmpty(error);
    }

}
