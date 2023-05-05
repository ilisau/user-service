package com.solvd.userservice.domain.event;

import com.solvd.userservice.domain.agregate.UserAggregate;
import reactor.core.publisher.Mono;

public interface Event {

    /**
     * Applies the event to the aggregate.
     *
     * @param aggregate aggregate
     * @return resulted aggregate
     */
    Mono<?> copyTo(Mono<UserAggregate> aggregate);

}
