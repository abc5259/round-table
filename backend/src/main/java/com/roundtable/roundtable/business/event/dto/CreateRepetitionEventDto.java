package com.roundtable.roundtable.business.event.dto;

import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Day;
import com.roundtable.roundtable.domain.event.RepetitionType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateRepetitionEventDto(
        String eventName,
        Category category,
        LocalDateTime startDateTime,
        List<Long> participantId,
        RepetitionDto repetitionDto
) {
    public record RepetitionDto(
            RepetitionType repetitionType,
            Integer repeatCycle,
            LocalDate repeatedUntilDate,
            List<Day> days
    ) {
    }
}
