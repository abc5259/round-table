package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
