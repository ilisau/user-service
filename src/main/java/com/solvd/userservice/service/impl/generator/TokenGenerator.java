package com.solvd.userservice.service.impl.generator;

import com.solvd.userservice.domain.User;

public interface TokenGenerator {

    /**
     * Generates a token.
     *
     * @param user user
     * @return token
     */
    String generate(User user);

}
