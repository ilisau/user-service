package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.MailData;
import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.service.JwtService;
import com.solvd.userservice.service.MailService;
import com.solvd.userservice.web.kafka.MessageSender;
import com.solvd.userservice.web.mapper.MailDataMapper;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MessageSender messageSender;
    private final MailDataMapper mailDataMapper;
    private final JwtService jwtService;

    @Override
    public void sendActivationMail(User user) {
        Map<String, Object> params = new HashMap<>();
        String token = jwtService.generateToken(JwtTokenType.ACTIVATION, user);
        params.put("token", token);
        params.put("user.name", user.getName());
        params.put("user.surname", user.getSurname());
        params.put("user.email", user.getEmail());
        params.put("user.id", user.getId());
        messageSender.sendMessage("mail",
                0,
                String.valueOf(user.hashCode()),
                mailDataMapper.toDto(new MailData(MailType.ACTIVATION, params))).subscribe();
    }

}
