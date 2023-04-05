package com.solvd.userservice.web.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface EventHandler {

    void handle(ConsumerRecord<String, Object> record, Acknowledgment acknowledgment);

}
