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

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final PasswordMapper passwordMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        user = userService.create(user);
        return userMapper.toDto(user);
    }

    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        user = userService.update(user);
        return userMapper.toDto(user);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @PutMapping("/{id}/password")
    public void changePassword(@PathVariable Long id, @RequestBody PasswordDto passwordDto) {
        Password password = passwordMapper.toEntity(passwordDto);
        userService.updatePassword(id, password);
    }

    @PostMapping("/{id}/password")
    public void setPassword(@PathVariable Long id, @RequestBody String newPassword) {
        userService.updatePassword(id, newPassword);
    }

    @GetMapping("/email/{email}")
    public UserDto getById(@PathVariable String email) {
        User user = userService.getByEmail(email);
        return userMapper.toDto(user);
    }

    @PostMapping("/activate")
    public void activate(@RequestBody JwtToken jwtToken) {
        userService.activate(jwtToken);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        userService.delete(id);
    }

}
