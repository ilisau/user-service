package com.solvd.userservice.web.kafka;

import com.solvd.userservice.web.dto.MailDataDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KafkaMessage {

    private String topic;
    private int partition;
    private String key;
    private MailDataDto data;

}
