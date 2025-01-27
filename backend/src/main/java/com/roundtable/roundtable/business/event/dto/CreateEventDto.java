package com.roundtable.roundtable.business.event.dto;

import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Day;
import com.roundtable.roundtable.domain.event.Days;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.RepetitionType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateEventDto(
        String eventName,
        Category category,
        LocalDateTime startDateTime,
        RepetitionDto repetitionDto,
        List<Long> participantIds
) {
    public record RepetitionDto(
            RepetitionType repetitionType,
            Integer repeatCycle,
            LocalDate repeatedUntilDate,
            List<Day> days
    ) {
        public Days getDays() {
            return new Days(days);
        }

        public Repetition toRepetition() {
            return Repetition.builder()
                    .repetitionType(repetitionType)
                    .repeatCycle(repeatCycle)
                    .repeatedUntilDate(repeatedUntilDate)
                    .daysOfWeeks(getDays().toDayOfWeeks())
                    .build();
        }
    }

    public boolean isRepetitionEvent() {
        return repetitionDto != null;
    }
}
