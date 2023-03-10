package com.solvd.userservice.service;

import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.User;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface MailService {

    Mono<Void> sendMail(User receiver, MailType mailType, Map<String, Object> params);

}
