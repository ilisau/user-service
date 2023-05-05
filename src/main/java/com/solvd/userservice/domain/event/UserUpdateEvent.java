package com.solvd.userservice.domain.event;

import com.solvd.userservice.domain.User;
import com.solvd.userservice.domain.agregate.UserAggregate;
import jakarta.persistence.Entity;
import reactor.core.publisher.Mono;

@Entity
public class UserUpdateEvent extends AbstractEvent {

    @Override
    public Mono<UserAggregate> copyTo(final Mono<UserAggregate> aggregate) {
        User user = (User) super.getPayload();
        return aggregate.map(u -> {
            u.setName(user.getName());
            u.setSurname(user.getSurname());
            u.setEmail(user.getEmail());
            return u;
        });
    }

}
