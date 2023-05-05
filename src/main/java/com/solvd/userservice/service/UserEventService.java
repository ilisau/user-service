package com.solvd.userservice.service;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.jwt.JwtToken;
import reactor.core.publisher.Mono;

public interface UserEventService {

    /**
     * Update user.
     *
     * @param user user
     * @return empty response
     */
    Mono<Void> update(User user);

    /**
     * Set new password.
     *
     * @param userId      userId
     * @param newPassword new password
     * @return empty response
     */
    Mono<Void> updatePassword(String userId, String newPassword);

    /**
     * Update password.
     *
     * @param userId   userId
     * @param password password object
     * @return empty response
     */
    Mono<Void> updatePassword(String userId, Password password);

    /**
     * Create user.
     *
     * @param user user
     * @return empty response
     */
    Mono<Void> create(User user);

    /**
     * Activate user.
     *
     * @param token token
     * @return empty response
     */
    Mono<Void> activate(JwtToken token);

    /**
     * Delete user.
     *
     * @param id id
     * @return empty response
     */
    Mono<Void> delete(String id);

}
