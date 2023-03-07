package com.solvd.userservice.service.impl;

import com.solvd.userservice.domain.MailType;
import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.exception.InvalidTokenException;
import com.solvd.userservice.domain.exception.PasswordMismatchException;
import com.solvd.userservice.domain.exception.UserAlreadyExistsException;
import com.solvd.userservice.domain.exception.UserNotFoundException;
import com.solvd.userservice.domain.jwt.JwtToken;
import com.solvd.userservice.repository.UserRepository;
import com.solvd.userservice.service.JwtService;
import com.solvd.userservice.service.MailService;
import com.solvd.userservice.service.UserService;
import com.solvd.userservice.web.security.jwt.JwtTokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailService mailService;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " not found"));
    }

    @Override
    @Transactional
    public User update(User user) {
        Optional<User> userWithSameEmail = userRepository.findByEmail(user.getEmail());
        if (userWithSameEmail.isPresent() && !Objects.equals(userWithSameEmail.get().getId(), user.getId())) {
            throw new UserAlreadyExistsException("user with email " + user.getEmail() + " already exists");
        }
        User oldUser = getById(user.getId());
        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setSurname(user.getSurname());
        userRepository.save(oldUser);
        return oldUser;
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        User user = getById(userId);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updatePassword(Long userId, Password password) {
        User user = getById(userId);
        if (!passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
            throw new PasswordMismatchException("old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(password.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User create(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("user with email " + user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActivated(false);
        userRepository.save(user);
        Map<String, Object> params = new HashMap<>();
        String token = jwtService.generateToken(JwtTokenType.ACTIVATION, user);
        params.put("token", token);
        mailService.sendMail(user, MailType.ACTIVATION, params);
        return user;
    }

    @Override
    @Transactional
    public void activate(JwtToken token) {
        if (!jwtService.validateToken(token.getToken())) {
            throw new InvalidTokenException("token is expired");
        }
        if (!jwtService.isTokenType(token.getToken(), JwtTokenType.ACTIVATION)) {
            throw new InvalidTokenException("invalid reset token");
        }
        Long id = jwtService.retrieveUserId(token.getToken());
        User user = getById(id);
        user.setActivated(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
