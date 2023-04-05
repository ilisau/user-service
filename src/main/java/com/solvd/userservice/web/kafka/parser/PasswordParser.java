package com.solvd.userservice.web.kafka.parser;

import com.google.gson.internal.LinkedTreeMap;
import com.solvd.userservice.domain.Password;
import org.springframework.stereotype.Component;

@Component
public class PasswordParser implements Parser<Password> {

    public Password parse(LinkedTreeMap<String, String> linkedTreeMap) {
        Password password = new Password();
        password.setNewPassword(linkedTreeMap.get("newPassword"));
        password.setOldPassword(linkedTreeMap.get("oldPassword"));
        return password;
    }

}
