package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
}
