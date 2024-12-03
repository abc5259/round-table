package com.roundtable.roundtable.domain.event.repository;

import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventDateTimeSlotRepository extends JpaRepository<EventDateTimeSlot, Long> {

    @Query("select e from EventDateTimeSlot e join e.event where e.id = :eventDateTimeId")
    Optional<EventDateTimeSlot> findByIdWithEvent(@Param("eventDateTimeId") Long eventDateTimeId);
}
