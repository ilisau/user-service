package com.solvd.userservice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class MailData {

    private MailType mailType;
    private Map<String, Object> params;

}
