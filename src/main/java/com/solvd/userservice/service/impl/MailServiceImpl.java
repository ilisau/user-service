package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.MailData;
import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.service.MailService;
import com.solvd.userservice.web.mapper.MailDataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final WebClient.Builder webClientBuilder;
    private final MailDataMapper mailDataMapper;

    @Override
    public Mono<Void> sendMail(MailType mailType, Map<String, Object> params) {
        MailData mailData = new MailData(mailType, params);
        return Mono.just(mailData)
                .flatMap(body -> webClientBuilder.build()
                        .post()
                        .uri("http://mail-client/api/v1/emails")
                        .bodyValue(mailDataMapper.toDto(mailData))
                        .retrieve()
                        .bodyToMono(Void.class));
    }

}
