package com.roundtable.roundtable.domain.event;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Embeddable
public record Repetition(
        @NotNull
        @Enumerated(EnumType.STRING)
        RepetitionType repetitionType,

        @NotNull
        Integer repeatCycle,

        @NotNull
        LocalDate repeatedUntilDate
) {
    public static Repetition of(RepetitionType repetitionType, Integer repeatCycle, LocalDate repeatedUntilDate) {
        return new Repetition(repetitionType, repeatCycle, repeatedUntilDate);
    }
}
