package com.solvd.userservice.domain.event;

import com.solvd.userservice.domain.agregate.UserAggregate;
import jakarta.persistence.Entity;
import reactor.core.publisher.Mono;

@Entity
public class ResetPasswordEvent extends AbstractEvent {

    @Override
    public Mono<UserAggregate> copyTo(Mono<UserAggregate> aggregate) {
        String password = (String) payload;
        return aggregate.map(u -> {
                    u.setPassword(password);
                    return u;
                });
    }

}
