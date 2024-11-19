package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventDateTimeSlotRepository extends JpaRepository<EventDateTimeSlot, Long> {
}
