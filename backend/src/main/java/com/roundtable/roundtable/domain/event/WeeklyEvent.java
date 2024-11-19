package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.event.adjuster.WeeklyStartDateTimeAdjuster;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("weekly")
@Getter
public class WeeklyEvent extends Event {

    public WeeklyEvent(
            LocalDate now,
            String name,
            Category category,
            LocalDateTime startDateTime,
            Repetition repetition,
            House house,
            Member creator,
            List<DayOfWeek> daysOfWeeks) {
        super(
                name, category,
                new WeeklyStartDateTimeAdjuster(now, startDateTime, daysOfWeeks).adjustStartDateTime(),
                repetition, house, creator);
    }

    public static WeeklyEvent from(LocalDate now,
                                   String name,
                                   Category category,
                                   LocalDateTime startDateTime,
                                   Repetition repetition,
                                   House house,
                                   Member creator,
                                   List<DayOfWeek> daysOfWeeks) {
        return new WeeklyEvent(now, name, category, startDateTime, repetition, house, creator, daysOfWeeks);
    }
}
