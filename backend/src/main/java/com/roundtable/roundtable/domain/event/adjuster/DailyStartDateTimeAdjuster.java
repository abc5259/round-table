package com.roundtable.roundtable.domain.event.adjuster;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DailyStartDateTimeAdjuster implements StartDateTimeAdjuster {

    private final LocalDate now;
    private final LocalDateTime startDateTime;

    public DailyStartDateTimeAdjuster(LocalDate now, LocalDateTime startDateTime) {
        this.now = now;
        this.startDateTime = startDateTime;
    }

    @Override
    public LocalDateTime adjustStartDateTime() {
        LocalDate laterDate = getLaterDate(now, startDateTime.toLocalDate());
        return LocalDateTime.of(laterDate, startDateTime.toLocalTime());
    }

    private LocalDate getLaterDate(LocalDate date1, LocalDate date2) {
        return date1.isAfter(date2) ? date1 : date2;
    }
}
