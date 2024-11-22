package com.roundtable.roundtable.presentation.event.request;

import com.roundtable.roundtable.business.event.dto.CreateEventDto;
import com.roundtable.roundtable.business.event.dto.CreateEventDto.RepetitionDto;
import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Day;
import com.roundtable.roundtable.domain.event.RepetitionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateEventRequest(
        @NotBlank
        String eventName,
        @NotNull
        Category category,
        @NotNull
        LocalDateTime startDateTime,
        Repetition repetition,
        @Min(1)
        List<Long> participantIds
) {
    public record Repetition(
            @NotNull
            RepetitionType repetitionType,
            @NotNull
            Integer repeatCycle,
            LocalDate repeatedUntilDate,
            List<Day> days
    ) {
    }

    public CreateEventDto toCreateEventDto() {
        return new CreateEventDto(
                eventName,
                category,
                startDateTime,
                new RepetitionDto(repetition.repetitionType, repetition.repeatCycle, repetition.repeatedUntilDate,
                        repetition.days),
                participantIds
        );
    }
}
