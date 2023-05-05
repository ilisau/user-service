package com.solvd.userservice.web.mapper;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.web.dto.PasswordDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordMapper {

    /**
     * Maps a PasswordDto to a Password.
     *
     * @param dto PasswordDto
     * @return Password
     */
    Password toEntity(PasswordDto dto);

    /**
     * Maps a Password to a PasswordDto.
     *
     * @param password Password
     * @return PasswordDto
     */
    PasswordDto toDto(Password password);

}
