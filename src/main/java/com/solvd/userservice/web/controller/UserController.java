package com.solvd.userservice.web.controller;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.jwt.JwtToken;
import com.solvd.userservice.service.UserEventService;
import com.solvd.userservice.service.UserQueryService;
import com.solvd.userservice.web.dto.PasswordDto;
import com.solvd.userservice.web.dto.UserDto;
import com.solvd.userservice.web.dto.validation.OnCreate;
import com.solvd.userservice.web.dto.validation.OnUpdate;
import com.solvd.userservice.web.mapper.PasswordMapper;
import com.solvd.userservice.web.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserEventService userEventService;
    private final UserQueryService userQueryService;
    private final UserMapper userMapper;
    private final PasswordMapper passwordMapper;

    /**
     * Create user.
     *
     * @param userDto user dto
     * @return empty response
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> create(
            @Validated(OnCreate.class) @RequestBody final UserDto userDto
    ) {
        User user = userMapper.toEntity(userDto);
        return userEventService.create(user);
    }

    /**
     * Update user.
     *
     * @param userDto user dto
     * @return empty response
     */
    @PutMapping
    public Mono<Void> update(
            @Validated(OnUpdate.class) @RequestBody final UserDto userDto
    ) {
        User user = userMapper.toEntity(userDto);
        return userEventService.update(user);
    }

    /**
     * Get user by id.
     *
     * @param id user id
     * @return user dto
     */
    @GetMapping("/{id}")
    public Mono<UserDto> getById(@PathVariable final String id) {
        Mono<User> user = userQueryService.getById(id);
        return user.map(userMapper::toDto);
    }

    /**
     * Change user password.
     *
     * @param id          user id
     * @param passwordDto password dto
     * @return empty response
     */
    @PutMapping("/{id}/password")
    public Mono<Void> changePassword(@PathVariable final String id,
                                     @RequestBody final PasswordDto passwordDto
    ) {
        Password password = passwordMapper.toEntity(passwordDto);
        return userEventService.updatePassword(id, password);
    }

    /**
     * Set user password.
     *
     * @param id          user id
     * @param newPassword new password
     * @return empty response
     */
    @PostMapping("/{id}/password")
    public Mono<Void> setPassword(@PathVariable final String id,
                                  @RequestBody final String newPassword) {
        return userEventService.updatePassword(id, newPassword);
    }

    /**
     * Get user by email.
     *
     * @param email user email
     * @return user dto
     */
    @GetMapping("/email/{email}")
    public Mono<UserDto> getByEmail(@PathVariable final String email) {
        Mono<User> user = userQueryService.getByEmail(email);
        return user.map(userMapper::toDto);
    }

    /**
     * Activate user.
     *
     * @param jwtToken jwt token
     * @return empty response
     */
    @PostMapping("/activate")
    public Mono<Void> activate(@RequestBody final JwtToken jwtToken) {
        return userEventService.activate(jwtToken);
    }

    /**
     * Delete user by id.
     *
     * @param id user id
     * @return empty response
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable final String id) {
        return userEventService.delete(id);
    }

}
