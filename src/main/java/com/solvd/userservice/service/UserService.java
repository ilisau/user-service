package com.solvd.userservice.service;

import com.solvd.userservice.domain.User;

public interface UserService {

    User getById(Long id);

    User getByEmail(String email);

    User create(User user);

    User update(User user);

    void delete(Long id);

}
