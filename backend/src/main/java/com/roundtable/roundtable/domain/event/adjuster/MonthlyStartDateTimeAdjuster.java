package com.roundtable.roundtable.domain.event.adjuster;

import java.time.LocalDateTime;

public class MonthlyStartDateTimeAdjuster implements StartDateTimeAdjuster {

    private final LocalDateTime now;
    private final LocalDateTime inputStartDateTime;

    public MonthlyStartDateTimeAdjuster(LocalDateTime now, LocalDateTime inputStartDateTime) {
        this.now = now;
        this.inputStartDateTime = inputStartDateTime;
    }

    @Override
    public LocalDateTime adjustStartDateTime() {
        if (inputStartDateTime.isAfter(now)) {
            return inputStartDateTime;
        }

        return inputStartDateTime.plusMonths(1);
    }
}
