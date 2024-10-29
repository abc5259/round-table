package com.roundtable.roundtable.business.event.dto;

import com.roundtable.roundtable.domain.event.Category;
import java.time.LocalDateTime;
import java.util.List;

public record CreateEventDto(
        String eventName,
        Category category,
        LocalDateTime startDateTime,
        List<Long> participantIds
) {
}
