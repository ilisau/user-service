package com.solvd.userservice.service;

import com.solvd.userservice.domain.MailType;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface MailService {

    Mono<Void> sendMail(MailType mailType, Map<String, Object> params);

}
