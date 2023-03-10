package com.solvd.userservice.web.dto;

import com.solvd.userservice.domain.MailType;
import lombok.Data;

import java.util.Map;

@Data
public class MailDataDto {

    private MailType mailType;
    private Map<String, Object> params;

}
