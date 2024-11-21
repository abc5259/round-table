package com.roundtable.roundtable.domain.event.adjuster;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class WeeklyStartDateTimeAdjuster implements StartDateTimeAdjuster {

    private final LocalDate now;
    private final LocalDateTime startDateTime;
    private final List<DayOfWeek> dayOfWeeks;

    public WeeklyStartDateTimeAdjuster(LocalDate now,
                                       LocalDateTime startDateTime,
                                       List<DayOfWeek> dayOfWeeks) {
        this.now = now;
        this.startDateTime = startDateTime;
        this.dayOfWeeks = dayOfWeeks;
    }

    @Override
    public LocalDateTime adjustStartDateTime() {
        LocalDate laterDate = getLaterDate(now, startDateTime.toLocalDate());
        return LocalDateTime.of(nextOrSameClosestDate(laterDate, dayOfWeeks), startDateTime.toLocalTime());
    }

    private LocalDate getLaterDate(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }

    private LocalDate nextOrSameClosestDate(LocalDate date, List<DayOfWeek> daysOfWeek) {
        LocalDate closestDate = null;

        for (DayOfWeek day : daysOfWeek) {
            LocalDate adjustedDate = date.with(TemporalAdjusters.nextOrSame(day));
            
            if (closestDate == null || adjustedDate.isBefore(closestDate)) {
                closestDate = adjustedDate;
            }
        }

        return closestDate;
    }
}
