package com.solvd.userservice.domain.agregate;

import com.solvd.userservice.domain.User;
import reactor.core.publisher.Mono;

public abstract class AggregateFactory {

    public static Mono<User> toUser(UserAggregate aggregate) {
        User user = new User();
        user.setId(aggregate.getId());
        user.setName(aggregate.getName());
        user.setSurname(aggregate.getSurname());
        user.setEmail(aggregate.getEmail());
        user.setPassword(aggregate.getPassword());
        user.setRole(aggregate.getRole());
        user.setActivated(aggregate.isActivated());
        return Mono.just(user);
    }

    public static Mono<UserAggregate> toAggregate(Mono<User> user) {
        return user.map(u -> {
            UserAggregate aggregate = new UserAggregate();
            aggregate.setId(u.getId());
            aggregate.setName(u.getName());
            aggregate.setSurname(u.getSurname());
            aggregate.setEmail(u.getEmail());
            aggregate.setPassword(u.getPassword());
            aggregate.setRole(u.getRole());
            aggregate.setActivated(u.isActivated());
            return aggregate;
        });
    }

}
