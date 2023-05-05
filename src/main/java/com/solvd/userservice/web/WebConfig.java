package com.solvd.userservice.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@RequiredArgsConstructor
public class WebConfig {

    /**
     * Create password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Get xml producer file.
     *
     * @return the xml
     */
    @SneakyThrows
    @Bean
    public XML producerXml() {
        return new XMLDocument(
                new File("src/main/resources/kafka/producer.xml")
        );
    }

    /**
     * Create json builder.
     *
     * @return the xml
     */
    @Bean
    public Gson gson() {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        return new GsonBuilder().registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>)
                                (json1, typeOfT, context) ->
                                        LocalDateTime.parse(
                                                json1.getAsString(),
                                                DateTimeFormatter
                                                        .ofPattern(pattern)
                                        ))
                .create();
    }

    /**
     * Create redis operations.
     *
     * @param factory the factory
     * @return the reactive redis operations
     */
    @Bean
    public ReactiveRedisOperations<String, User> redisOperations(
            final ReactiveRedisConnectionFactory factory
    ) {
        Jackson2JsonRedisSerializer<User> serializer =
                new Jackson2JsonRedisSerializer<>(User.class);
        RedisSerializationContext
                .RedisSerializationContextBuilder<String, User> builder =
                RedisSerializationContext.newSerializationContext(
                        new StringRedisSerializer());
        RedisSerializationContext<String, User> context =
                builder.value(serializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }

}
