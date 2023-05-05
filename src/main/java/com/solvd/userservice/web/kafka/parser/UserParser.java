package com.solvd.userservice.web.kafka.parser;

import com.google.gson.internal.LinkedTreeMap;
import com.solvd.userservice.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserParser implements Parser<User> {

    /**
     * Parse the map to the user.
     *
     * @param linkedTreeMap the map
     * @return the user
     */
    public User parse(final LinkedTreeMap<String, String> linkedTreeMap) {
        User user = new User();
        user.setId(linkedTreeMap.get("id"));
        user.setName(linkedTreeMap.get("name"));
        user.setSurname(linkedTreeMap.get("surname"));
        user.setEmail(linkedTreeMap.get("email"));
        if (linkedTreeMap.get("role") != null) {
            user.setRole(User.Role.valueOf(linkedTreeMap.get("role")));
        }
        user.setPassword(linkedTreeMap.get("password"));
        user.setActivated(Boolean.getBoolean(linkedTreeMap.get("isActivated")));
        return user;
    }

}
