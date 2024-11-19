package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.event.adjuster.DailyStartDateTimeAdjuster;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("daily")
@Getter
public class DailyEvent extends Event {

    public DailyEvent(
            LocalDate now,
            String name,
            Category category,
            LocalDateTime startDateTime,
            Repetition repetition,
            House house,
            Member creator) {
        super(
                name, category,
                new DailyStartDateTimeAdjuster(now, startDateTime).adjustStartDateTime(),
                repetition, house, creator);
    }

    public static DailyEvent from(LocalDate now,
                                  String name,
                                  Category category,
                                  LocalDateTime startDateTime,
                                  Repetition repetition,
                                  House house,
                                  Member creator) {
        return new DailyEvent(now, name, category, startDateTime, repetition, house, creator);
    }
}
