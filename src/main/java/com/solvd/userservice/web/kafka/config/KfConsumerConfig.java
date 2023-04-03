package com.solvd.userservice.web.kafka.config;

import com.jcabi.xml.XML;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KfConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    private final XML settings;

    public KfConsumerConfig(@Qualifier("consumerXml") XML settings) {
        this.settings = settings;
    }

    @Bean
    public ReceiverOptions<String, Object> receiverOptions() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(
                "group.id",
                new TextXpath(
                        this.settings, "//groupId"
                ).toString()
        );
        props.put(
                "key.deserializer",
                new TextXpath(
                        this.settings, "//keyDeserializer"
                ).toString()
        );
        props.put(
                "value.deserializer",
                new TextXpath(
                        this.settings, "//valueDeserializer"
                ).toString()
        );
        props.put(
                "spring.json.trusted.packages",
                new TextXpath(
                        this.settings, "//trustedPackages"
                ).toString()
        );
        ReceiverOptions<String, Object> receiverOptions = ReceiverOptions.create(props);
        return receiverOptions.subscription(Collections.singleton("events"))
                .addAssignListener(partitions -> System.out.println("onPartitionAssigned: " + partitions))
                .addRevokeListener(partitions -> System.out.println("onPartitionRevoked: " + partitions));
    }

    @Bean
    public KafkaReceiver<String, Object> receiver(ReceiverOptions<String, Object> receiverOptions) {
        return KafkaReceiver.create(receiverOptions);
    }

}
