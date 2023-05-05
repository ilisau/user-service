package com.solvd.userservice.web.mapper;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.web.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Maps a UserDto to a User.
     *
     * @param user User
     * @return UserDto
     */
    UserDto toDto(User user);

    /**
     * Maps a User to a UserDto.
     *
     * @param userDto UserDto
     * @return User
     */
    User toEntity(UserDto userDto);

}
