package com.solvd.userservice.domain.event;

import com.solvd.userservice.domain.agregate.UserAggregate;
import jakarta.persistence.Entity;
import reactor.core.publisher.Mono;

@Entity
public class UserActivateEvent extends AbstractEvent {

    @Override
    public Mono<UserAggregate> copyTo(final Mono<UserAggregate> aggregate) {
        return aggregate.map(u -> {
            u.setActivated(true);
            return u;
        });
    }

}
