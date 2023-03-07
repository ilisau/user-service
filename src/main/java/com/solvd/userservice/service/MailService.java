package com.solvd.userservice.service;

import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.User;

import java.util.Map;

public interface MailService {

    void sendMail(User receiver, MailType mailType, Map<String, Object> params);

}
