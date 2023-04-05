package com.solvd.userservice.domain.event;

import com.solvd.userservice.domain.Password;
import com.solvd.userservice.domain.agregate.UserAggregate;
import jakarta.persistence.Entity;
import reactor.core.publisher.Mono;

@Entity
public class UpdatePasswordEvent extends AbstractEvent {

    @Override
    public Mono<UserAggregate> copyTo(Mono<UserAggregate> aggregate) {
        Password password = (Password) payload;
        return aggregate.map(u -> {
                    u.setPassword(password.getNewPassword());
                    return u;
                });
    }

}
