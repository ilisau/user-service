package com.solvd.userservice.web.mapper;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.web.dto.PasswordDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordMapper {

    Password toEntity(PasswordDto dto);

    PasswordDto toDto(Password password);

}
