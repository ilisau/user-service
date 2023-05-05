package com.solvd.userservice.repository;

import com.solvd.userservice.domain.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    /**
     * Find user by email.
     *
     * @param email email of user
     * @return user
     */
    Mono<User> findByEmail(String email);

}
