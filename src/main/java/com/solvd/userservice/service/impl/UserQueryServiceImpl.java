package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;
    private final ReactiveRedisOperations<String, User> userOps;

    @Override
    public Mono<User> getById(final String id) {
        Mono<User> error = Mono.error(
                new UserNotFoundException("User with id " + id + " not found")
        );
        return userOps.opsForValue()
                .get(id)
                .switchIfEmpty(userRepository.findById(id)
                        .switchIfEmpty(error)
                        .onErrorResume(Mono::error)
                        .map(u -> {
                            userOps.opsForValue().set(id, u).subscribe();
                            return u;
                        }))
                .flatMap(u -> userOps.opsForValue().get(id));
    }

    @Override
    public Mono<User> getByEmail(final String email) {
        Mono<User> error = Mono.error(
                new UserNotFoundException("User with email " + email
                        + " not found")
        );
        return userRepository.findByEmail(email)
                .switchIfEmpty(error);
    }

}
