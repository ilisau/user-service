package com.solvd.userservice.web.kafka;

import com.solvd.userservice.domain.event.AbstractEvent;
import com.solvd.userservice.web.dto.MailDataDto;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.SenderResult;

public interface MessageSender {

    /**
     * Send a message to the topic.
     *
     * @param message the message
     * @param data    the data
     * @return the result
     */
    Flux<SenderResult<MailDataDto>> sendMessage(KafkaMessage message,
                                                MailDataDto data);

    /**
     * Send a message to the topic.
     *
     * @param message the message
     * @param event   the event
     * @return the result
     */
    Flux<SenderResult<AbstractEvent>> sendMessage(KafkaMessage message,
                                                  AbstractEvent event);

}
