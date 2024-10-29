package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.EventDayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDayOfWeekRepository extends JpaRepository<EventDayOfWeek, Long> {
}
