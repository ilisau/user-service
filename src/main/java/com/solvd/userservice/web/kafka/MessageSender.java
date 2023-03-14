package com.solvd.userservice.web.kafka;

import reactor.core.publisher.Flux;
import reactor.kafka.sender.SenderResult;

public interface MessageSender {

    Flux<SenderResult<Object>> sendMessage(String topic, int partition, String key, Object data);

}
