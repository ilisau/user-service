package com.solvd.userservice.web.mapper;

import com.solvd.userservice.domain.MailData;
import com.solvd.userservice.web.dto.MailDataDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailDataMapper {

    /**
     * Maps a MailDataDto to a MailData.
     *
     * @param entity MailData
     * @return MailDataDto
     */
    MailDataDto toDto(MailData entity);

    /**
     * Maps a MailData to a MailDataDto.
     *
     * @param userDto MailDataDto
     * @return MailData
     */
    MailData toEntity(MailDataDto userDto);

}
