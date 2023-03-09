package com.solvd.userservice.web.controller;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.jwt.JwtToken;
import com.solvd.userservice.service.UserService;
import com.solvd.userservice.web.dto.PasswordDto;
import com.solvd.userservice.web.dto.UserDto;
import com.solvd.userservice.web.dto.validation.OnCreate;
import com.solvd.userservice.web.dto.validation.OnUpdate;
import com.solvd.userservice.web.mapper.PasswordMapper;
import com.solvd.userservice.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordMapper passwordMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> create(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        Mono<User> userMono = userService.create(user);
        return userMono.map(userMapper::toDto);
    }

    @PutMapping
    public Mono<UserDto> update(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        Mono<User> userMono = userService.update(user);
        return userMono.map(userMapper::toDto);
    }

    @GetMapping("/{id}")
    public Mono<UserDto> getById(@PathVariable Long id) {
        Mono<User> user = userService.getById(id);
        return user.map(userMapper::toDto);
    }

    @PutMapping("/{id}/password")
    public Mono<Void> changePassword(@PathVariable Long id,
                               @RequestBody PasswordDto passwordDto) {
        Password password = passwordMapper.toEntity(passwordDto);
        return userService.updatePassword(id, password);
    }

    @PostMapping("/{id}/password")
    public Mono<Void> setPassword(@PathVariable Long id,
                            @RequestBody String newPassword) {
        return userService.updatePassword(id, newPassword);
    }

    @GetMapping("/email/{email}")
    public Mono<UserDto> getById(@PathVariable String email) {
        Mono<User> user = userService.getByEmail(email);
        return user.map(userMapper::toDto);
    }

    @PostMapping("/activate")
    public Mono<Void> activate(@RequestBody JwtToken jwtToken) {
        return userService.activate(jwtToken);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable Long id) {
        return userService.delete(id);
    }

}
