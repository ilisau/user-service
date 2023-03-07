package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.service.MailService;
import com.solvd.userservice.service.property.MailLinkProperties;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final Configuration configuration;
    private final JavaMailSender mailSender;
    private final MailLinkProperties mailLinkProperties;

    @Override
    @SneakyThrows
    public void sendMail(User user, MailType mailType, Map<String, Object> params) {
        switch (mailType) {
            case ACTIVATION -> sendActivationMail(user, params);
            case PASSWORD_RESET -> sendPasswordResetMail(user, params);
        }
    }

    @SneakyThrows
    private void sendActivationMail(User user, Map<String, Object> params) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Welcome to QaproTours");
        helper.setTo(user.getEmail());
        String emailContent = getActivationEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private void sendPasswordResetMail(User user, Map<String, Object> params) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject("Restore password");
        helper.setTo(user.getEmail());
        String emailContent = getRestoreEmailContent(user, params);
        helper.setText(emailContent, true);
        mailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getActivationEmailContent(User user, Map<String, Object> params) {
        StringWriter stringWriter = new StringWriter();

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName() + " " + user.getSurname());
        model.put("link", mailLinkProperties.getActivation() + params.get("token"));
        configuration.getTemplate("activation.ftlh")
                .process(model, stringWriter);
        return stringWriter.getBuffer()
                .toString();
    }

    @SneakyThrows
    private String getRestoreEmailContent(User user, Map<String, Object> params) {
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName() + " " + user.getSurname());
        model.put("link", mailLinkProperties.getRestore() + params.get("token"));
        configuration.getTemplate("restore.ftlh")
                .process(model, stringWriter);
        return stringWriter.getBuffer()
                .toString();
    }

}
