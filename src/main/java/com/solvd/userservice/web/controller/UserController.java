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
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
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
     * @param user user dto
     * @return empty response
     */
    @PostMapping
    @MutationMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> createUser(
            @Validated(OnCreate.class) @RequestBody @Argument final UserDto user
    ) {
        User u = userMapper.toEntity(user);
        return userEventService.create(u);
    }

    /**
     * Update user.
     *
     * @param user user dto
     * @return empty response
     */
    @PutMapping
    @MutationMapping
    public Mono<Void> updateUser(
            @Validated(OnUpdate.class) @RequestBody @Argument final UserDto user
    ) {
        User u = userMapper.toEntity(user);
        return userEventService.update(u);
    }

    /**
     * Get user by id.
     *
     * @param id user id
     * @return user dto
     */
    @GetMapping("/{id}")
    @QueryMapping
    public Mono<UserDto> userById(@PathVariable @Argument final String id) {
        Mono<User> user = userQueryService.getById(id);
        return user.map(userMapper::toDto);
    }

    /**
     * Change user password.
     *
     * @param id       user id
     * @param password password dto
     * @return empty response
     */
    @PutMapping("/{id}/password")
    @MutationMapping
    public Mono<Void> changePassword(@Argument @PathVariable final String id,
                                     @Argument
                                     @RequestBody final PasswordDto password
    ) {
        Password p = passwordMapper.toEntity(password);
        return userEventService.updatePassword(id, p);
    }

    /**
     * Set user password.
     *
     * @param id          user id
     * @param newPassword new password
     * @return empty response
     */
    @PostMapping("/{id}/password")
    @MutationMapping
    public Mono<Void> setPassword(@Argument @PathVariable final String id,
                                  @Argument
                                  @RequestBody final String newPassword
    ) {
        return userEventService.updatePassword(id, newPassword);
    }

    /**
     * Get user by email.
     *
     * @param email user email
     * @return user dto
     */
    @GetMapping("/email/{email}")
    @QueryMapping
    public Mono<UserDto> userByEmail(@PathVariable @Argument final String email
    ) {
        Mono<User> user = userQueryService.getByEmail(email);
        return user.map(userMapper::toDto);
    }

    /**
     * Activate user.
     *
     * @param token jwt token
     * @return empty response
     */
    @PostMapping("/activate")
    @MutationMapping
    public Mono<Void> activateUser(@RequestBody @Argument final JwtToken token
    ) {
        return userEventService.activate(token);
    }

    /**
     * Delete user by id.
     *
     * @param id user id
     * @return empty response
     */
    @DeleteMapping("/{id}")
    @MutationMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUser(@PathVariable @Argument final String id) {
        return userEventService.delete(id);
    }

}
