package com.solvd.userservice.web.kafka;

import com.jcabi.xml.XML;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KfProducerConfig {

    private final XML settings;

    @Bean
    public NewTopic topicMail() {
        return TopicBuilder.name("mail")
                .partitions(5)
                .replicas(1)
                .build();
    }

    @Bean
    public SenderOptions<String, Object> senderOptions() {
        Map<String, Object> props = new HashMap<>(3);
        props.put(
                "bootstrap.servers",
                new TextXpath(this.settings, "//bootstrapServers")
                        .toString()
        );
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