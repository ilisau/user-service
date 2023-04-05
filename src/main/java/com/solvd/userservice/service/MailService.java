package com.solvd.userservice.service;

import com.solvd.userservice.domain.User;

public interface MailService {

    void sendActivationMail(User user);

}
