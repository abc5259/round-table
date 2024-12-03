package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDayOfWeek;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDayOfWeekRepository extends JpaRepository<EventDayOfWeek, Long> {

    List<EventDayOfWeek> findByEvent(Event event);
}
