package com.roundtable.roundtable.domain.event.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.roundtable.roundtable.domain.event.Category;
import java.time.LocalDateTime;

public record EventDateTimeSlotDetailDto(
        Long eventId,
        Long eventDateTimeSlotId,
        String name,
        Category category,
        boolean isCompleted,
        LocalDateTime startDateTime,
        String participants
) {

    @QueryProjection
    public EventDateTimeSlotDetailDto {
    }
}
