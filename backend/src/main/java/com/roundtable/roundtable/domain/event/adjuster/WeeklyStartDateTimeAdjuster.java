package com.roundtable.roundtable.domain.event.adjuster;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class WeeklyStartDateTimeAdjuster implements StartDateTimeAdjuster {

    private final LocalDateTime now;
    private final LocalDateTime inputStartDateTime;
    private final List<DayOfWeek> dayOfWeeks;

    public WeeklyStartDateTimeAdjuster(LocalDateTime now, LocalDateTime inputStartDateTime,
                                       List<DayOfWeek> dayOfWeeks) {
        this.now = now;
        this.inputStartDateTime = inputStartDateTime;
        this.dayOfWeeks = dayOfWeeks;
    }

    @Override
    public LocalDateTime adjustStartDateTime() {
        LocalDateTime laterDate = getLaterDate(now, inputStartDateTime);
        return nextOrSameClosest(laterDate, dayOfWeeks);
    }

    private LocalDateTime getLaterDate(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        return dateTime1.isAfter(dateTime2) ? dateTime1 : dateTime2;
    }

    private LocalDateTime nextOrSameClosest(LocalDateTime date, List<DayOfWeek> daysOfWeek) {
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
