package com.roundtable.roundtable.business.event.dto;

import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Day;
import com.roundtable.roundtable.domain.event.Days;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.RepetitionType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateRepetitionEventDto(
        String eventName,
        Category category,
        LocalDateTime startDateTime,
        List<Long> participantId,
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
            return Repetition.of(
                    repetitionType(),
                    repeatCycle(),
                    repeatedUntilDate(),
                    getDays().toDayOfWeeks());
        }
    }
}
