package com.solvd.userservice.web.kafka.handler;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;

public interface EventHandler {

    /**
     * Handle the event.
     *
     * @param record         the record
     * @param acknowledgment the acknowledgment
     */
    void handle(ConsumerRecord<String, Object> record,
                Acknowledgment acknowledgment);

}
