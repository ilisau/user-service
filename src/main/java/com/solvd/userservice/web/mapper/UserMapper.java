package com.solvd.userservice.web.mapper;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.web.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

}
