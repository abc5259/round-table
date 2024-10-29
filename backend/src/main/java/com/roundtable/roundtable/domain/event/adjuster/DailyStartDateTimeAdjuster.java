package com.roundtable.roundtable.domain.event.adjuster;

import java.time.LocalDateTime;

public class DailyStartDateTimeAdjuster implements StartDateTimeAdjuster {

    private final LocalDateTime now;
    private final LocalDateTime inputStartDateTime;

    public DailyStartDateTimeAdjuster(LocalDateTime now, LocalDateTime inputStartDateTime) {
        this.now = now;
        this.inputStartDateTime = inputStartDateTime;
    }

    @Override
    public LocalDateTime adjustStartDateTime() {
        return getLaterDate(now, inputStartDateTime);
    }

    private LocalDateTime getLaterDate(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2) ? dateTime1 : dateTime2;
    }
}
