package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.EventTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTimeSlotRepository extends JpaRepository<EventTimeSlot, Long> {
}
