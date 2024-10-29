package com.roundtable.roundtable.domain.event;

import static com.roundtable.roundtable.domain.event.RepetitionType.DAILY;
import static com.roundtable.roundtable.domain.event.RepetitionType.WEEKLY;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
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

    public LocalDateTime decideStartDateTime(LocalDateTime now,
                                             LocalDateTime inputStartDateTime,
                                             List<DayOfWeek> dayOfWeeks) {
        if (repetitionType == DAILY) {
            return getLaterDate(now, inputStartDateTime);
        }

        if (repetitionType == WEEKLY) {
            LocalDateTime laterDate = getLaterDate(now, inputStartDateTime);
            return nextOrSameClosest(laterDate, dayOfWeeks);
        }

        if (inputStartDateTime.isAfter(now)) {
            return inputStartDateTime;
        }

        return inputStartDateTime.plusMonths(1);
    }

    private LocalDateTime getLaterDate(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2) ? dateTime1 : dateTime2;
    }

    public LocalDateTime nextOrSameClosest(LocalDateTime date, List<DayOfWeek> daysOfWeek) {
        LocalDateTime closestDate = null;

        for (DayOfWeek day : daysOfWeek) {
            // 주어진 날짜를 기준으로 지정된 요일로 이동
            LocalDateTime adjustedDate = date.with(TemporalAdjusters.nextOrSame(day));

            // 가장 가까운 날짜 찾기
            if (closestDate == null || adjustedDate.isBefore(closestDate)) {
                closestDate = adjustedDate;
            }
        }

        return closestDate;
    }
}
