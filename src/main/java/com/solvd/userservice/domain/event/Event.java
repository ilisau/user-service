package com.solvd.userservice.domain.event;

import com.solvd.userservice.domain.agregate.UserAggregate;
import reactor.core.publisher.Mono;

public interface Event {

    Mono<?> copyTo(Mono<UserAggregate> aggregate);

}
