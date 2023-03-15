package com.solvd.userservice.web.kafka;

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

    private final KafkaSender<String, MailDataDto> sender;

    @Override
    public Flux<SenderResult<MailDataDto>> sendMessage(String topic, int partition, String key, MailDataDto data) {
        return sender.send(
                Mono.just(
                        SenderRecord.create(
                                topic,
                                partition,
                                System.currentTimeMillis(),
                                key,
                                data,
                                null
                        )
                )
        );
    }

}
