package com.roundtable.roundtable.domain.event.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record EventDateTimeSlotDto(
        Long eventId,
        Long eventDateTimeSlotId,
        String name,
        boolean isCompleted,
        LocalDateTime startDateTime
) {

    @QueryProjection
    public EventDateTimeSlotDto {
    }
}
