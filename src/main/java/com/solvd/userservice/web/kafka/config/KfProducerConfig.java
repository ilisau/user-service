package com.solvd.userservice.web.kafka.config;

import com.jcabi.xml.XML;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KfProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String servers;

    private final XML settings;

    public KfProducerConfig(@Qualifier("producerXml") XML settings) {
        this.settings = settings;
    }

    @Bean
    public NewTopic topicMail() {
        return TopicBuilder.name("mail")
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic topicEvents() {
        return TopicBuilder.name("events")
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public SenderOptions<String, Object> senderOptions() {
        Map<String, Object> props = new HashMap<>(3);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(
                "key.serializer",
                new TextXpath(this.settings, "//keySerializer")
                        .toString()
        );
        props.put(
                "value.serializer",
                new TextXpath(this.settings, "//valueSerializer")
                        .toString()
        );
        return SenderOptions.create(props);
    }

    @Bean
    public KafkaSender<String, Object> sender(SenderOptions<String, Object> senderOptions) {
        return KafkaSender.create(senderOptions);
    }

}