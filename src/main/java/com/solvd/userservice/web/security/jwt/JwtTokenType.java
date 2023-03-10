package com.solvd.userservice.web.security.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtTokenType {

    ACCESS,
    REFRESH,
    ACTIVATION,
    RESET

}
