package com.solvd.userservice.web.kafka;

import com.solvd.userservice.domain.event.AbstractEvent;
import com.solvd.userservice.web.dto.MailDataDto;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.SenderResult;

public interface MessageSender {

    Flux<SenderResult<MailDataDto>> sendMessage(String topic, int partition, String key, MailDataDto data);

    Flux<SenderResult<AbstractEvent>> sendMessage(String topic, int partition, String key, AbstractEvent event);

}
