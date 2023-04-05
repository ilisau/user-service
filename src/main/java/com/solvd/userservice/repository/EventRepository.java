package com.solvd.userservice.repository;

import com.solvd.userservice.domain.event.AbstractEvent;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<AbstractEvent, Long> {
}
