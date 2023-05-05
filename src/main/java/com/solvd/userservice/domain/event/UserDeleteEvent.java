package com.solvd.userservice.domain.event;

import com.solvd.userservice.domain.agregate.UserAggregate;
import jakarta.persistence.Entity;
import reactor.core.publisher.Mono;

@Entity
public class UserDeleteEvent extends AbstractEvent {

    @Override
    public Mono<Void> copyTo(final Mono<UserAggregate> aggregate) {
        return Mono.empty();
    }

}
