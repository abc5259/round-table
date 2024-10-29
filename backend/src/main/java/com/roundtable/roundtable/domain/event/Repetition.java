package com.roundtable.roundtable.domain.event;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Repetition {

    private static final int MIN_REPEAT_CYCLE = 1;
    private static final int DAILY_MAX_CYCLE = 100;
    private static final int WEEKLY_MAX_CYCLE = 10;
    private static final int MONTHLY_MAX_CYCLE = 12;
    private static final int MAX_TIME_SLOT_SIZE = 30;

    @NotNull
    @Enumerated(EnumType.STRING)
    RepetitionType repetitionType;

    @NotNull
    Integer repeatCycle;

    @NotNull
    LocalDate repeatedUntilDate;

    private Repetition(RepetitionType repetitionType, Integer repeatCycle, LocalDate repeatedUntilDate) {
        validateRepeatCycle(repeatCycle);
        repeatCycle = getRepeatCycle(repetitionType, repeatCycle);

        this.repetitionType = repetitionType;
        this.repeatCycle = repeatCycle;
        this.repeatedUntilDate = repeatedUntilDate;
    }

    private Integer getRepeatCycle(RepetitionType repetitionType, Integer repeatCycle) {
        return switch (repetitionType) {
            case DAILY -> Math.max(repeatCycle, DAILY_MAX_CYCLE);
            case WEEKLY -> Math.max(repeatCycle, WEEKLY_MAX_CYCLE);
            case MONTHLY -> Math.max(repeatCycle, MONTHLY_MAX_CYCLE);
            default -> throw new IllegalStateException("Unexpected value: " + repetitionType);
        };

    }

    private void validateRepeatCycle(Integer repeatCycle) {
        if (repeatCycle < MIN_REPEAT_CYCLE) {
            throw new IllegalArgumentException("반복 주기는 1보다 크거나 같아야 합니다.");
        }
    }

    public static Repetition of(RepetitionType repetitionType, Integer repeatCycle, LocalDate repeatedUntilDate) {
        return new Repetition(repetitionType, repeatCycle, repeatedUntilDate);
    }
}
