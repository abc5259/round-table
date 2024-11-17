package com.roundtable.roundtable.domain.event.adjuster;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MonthlyStartDateTimeAdjuster implements StartDateTimeAdjuster {

    private final LocalDate now;
    private final LocalDateTime startDateTime;

    public MonthlyStartDateTimeAdjuster(LocalDate now, LocalDateTime startDateTime) {
        this.now = now;
        this.startDateTime = startDateTime;
    }

    @Override
    public LocalDateTime adjustStartDateTime() {
        if (startDateTime.toLocalDate().isAfter(now)) {
            return startDateTime;
        }

        return startDateTime.plusMonths(1);
    }
}
