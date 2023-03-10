package com.solvd.userservice.web.mapper;

import com.solvd.userservice.domain.MailData;
import com.solvd.userservice.web.dto.MailDataDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MailDataMapper {

    MailDataDto toDto(MailData entity);

    MailData toEntity(MailDataDto userDto);

}
