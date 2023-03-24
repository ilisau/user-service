package com.solvd.userservice.web;

import com.jcabi.xml.XML;
import com.jcabi.xml.XMLDocument;
import com.solvd.userservice.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.File;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Bean
    public XML producerXml() {
        return new XMLDocument(new File("src/main/resources/kafka/producer.xml"));
    }

    @Bean
    ReactiveRedisOperations<String, User> redisOperations(ReactiveRedisConnectionFactory factory) {
        Jackson2JsonRedisSerializer<User> serializer = new Jackson2JsonRedisSerializer<User>(User.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, User> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, User> context = builder.value(serializer)
                .hashValue(serializer)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}
