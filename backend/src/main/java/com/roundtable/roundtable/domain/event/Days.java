package com.roundtable.roundtable.domain.event;

import java.time.DayOfWeek;
import java.util.List;

public class Days {

    private final List<Day> days;

    public Days(List<Day> days) {
        this.days = days;
    }

    public List<DayOfWeek> toDayOfWeeks() {
        return days.stream()
                .map(Day::toDayOfWeek)
                .toList();
    }
}
