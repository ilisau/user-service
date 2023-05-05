package com.solvd.userservice.service;

import com.solvd.userservice.domain.User;

public interface MailService {

    /**
     * Send activation mail.
     *
     * @param user user
     */
    void sendActivationMail(User user);

}
