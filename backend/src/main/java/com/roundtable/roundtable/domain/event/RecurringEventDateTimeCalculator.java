package com.roundtable.roundtable.domain.event;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.Recur.Builder;
import net.fortuna.ical4j.model.WeekDay;
import net.fortuna.ical4j.transform.recurrence.Frequency;
import org.springframework.stereotype.Component;

@Component
public class RecurringEventDateTimeCalculator {

    public List<LocalDateTime> calculateRepetitionDates(LocalDateTime startDateTime,
                                                        Repetition repetition,
                                                        List<DayOfWeek> dayOfWeeks,
                                                        int count) {
        Recur<Temporal> recur = new Builder<>()
                .frequency(getFrequency(repetition.getRepetitionType()))
                .interval(repetition.getRepeatCycle())
                .dayList(getWeekDays
                        (dayOfWeeks))
                .count(count)
                .build();
        List<Temporal> dates = recur.getDates(startDateTime, repetition.getRepeatedUntilDate());
        return dates.stream()
                .map(date -> (LocalDateTime) date)
                .toList();
    }

    private List<WeekDay> getWeekDays(List<DayOfWeek> dayOfWeeks) {
        if (dayOfWeeks == null) {
            return new ArrayList<>();
        }
        return dayOfWeeks.stream().map(WeekDay::getWeekDay).toList();
    }

    private Frequency getFrequency(RepetitionType repetitionType) {
        if (repetitionType == RepetitionType.DAILY) {
            return Frequency.DAILY;
        }

        if (repetitionType == RepetitionType.WEEKLY) {
            return Frequency.WEEKLY;
        }

        return Frequency.MONTHLY;
    }
}
