package com.solvd.userservice.web.kafka;

import com.solvd.userservice.domain.event.AbstractEvent;
import com.solvd.userservice.web.dto.MailDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

@Component
@RequiredArgsConstructor
public class MessageSenderImpl implements MessageSender {

    private final KafkaSender<String, Object> sender;

    @Override
    public Flux<SenderResult<MailDataDto>> sendMessage(
            final KafkaMessage message,
            final MailDataDto data
    ) {
        return sender.send(
                Mono.just(
                        SenderRecord.create(
                                message.getTopic(),
                                message.getPartition(),
                                System.currentTimeMillis(),
                                message.getKey(),
                                data,
                                null
                        )
                )
        );
    }

    @Override
    public Flux<SenderResult<AbstractEvent>> sendMessage(
            final KafkaMessage message,
            final AbstractEvent event
    ) {
        return sender.send(
                Mono.just(
                        SenderRecord.create(
                                message.getTopic(),
                                message.getPartition(),
                                System.currentTimeMillis(),
                                message.getKey(),
                                event,
                                null
                        )
                )
        );
    }

}
