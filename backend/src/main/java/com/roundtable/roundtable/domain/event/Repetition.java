package com.roundtable.roundtable.domain.event;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Repetition {

    private static final int MIN_REPEAT_CYCLE = 1;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RepetitionType repetitionType;

    @NotNull
    private Integer repeatCycle;

    @NotNull
    private LocalDate repeatedUntilDate;

    @Transient
    private List<DayOfWeek> daysOfWeeks;

    @Builder
    private Repetition(RepetitionType repetitionType, Integer repeatCycle, LocalDate repeatedUntilDate,
                       List<DayOfWeek> daysOfWeeks) {
        validateRepeatCycle(repeatCycle);
        repeatCycle = repetitionType.calculateMinRepeatCycle(repeatCycle);

        this.repetitionType = repetitionType;
        this.repeatCycle = repeatCycle;
        // TODO: 종료일정 어디까지 허용될지 생각
        this.repeatedUntilDate = repeatedUntilDate;
        this.daysOfWeeks = daysOfWeeks;
    }

    private void validateRepeatCycle(Integer repeatCycle) {
        if (repeatCycle < MIN_REPEAT_CYCLE) {
            throw new IllegalArgumentException("반복 주기는 1보다 크거나 같아야 합니다.");
        }
    }

    public static Repetition of(RepetitionType repetitionType, Integer repeatCycle, LocalDate repeatedUntilDate) {
        return Repetition.builder()
                .repetitionType(repetitionType)
                .repeatCycle(repeatCycle)
                .repeatedUntilDate(repeatedUntilDate)
                .build();
    }

    public static Repetition of(RepetitionType repetitionType, Integer repeatCycle, LocalDate repeatedUntilDate,
                                List<DayOfWeek> dayOfWeeks) {
        return Repetition.builder()
                .repetitionType(repetitionType)
                .repeatCycle(repeatCycle)
                .repeatedUntilDate(repeatedUntilDate)
                .daysOfWeeks(dayOfWeeks)
                .build();
    }
}
