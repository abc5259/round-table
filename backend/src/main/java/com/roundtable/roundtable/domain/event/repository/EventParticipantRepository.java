package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.EventParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    List<EventParticipant> findAllByEventId(Long eventId);
}
