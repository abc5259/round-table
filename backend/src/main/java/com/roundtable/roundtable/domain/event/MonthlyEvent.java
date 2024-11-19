package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.event.adjuster.MonthlyStartDateTimeAdjuster;
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
@DiscriminatorValue("monthly")
@Getter
public class MonthlyEvent extends Event {

    public MonthlyEvent(
            LocalDate now,
            String name,
            Category category,
            LocalDateTime startDateTime,
            Repetition repetition,
            House house,
            Member creator) {
        super(
                name, category,
                new MonthlyStartDateTimeAdjuster(now, startDateTime).adjustStartDateTime(),
                repetition, house, creator);
    }

    public static MonthlyEvent from(LocalDate now,
                                    String name,
                                    Category category,
                                    LocalDateTime startDateTime,
                                    Repetition repetition,
                                    House house,
                                    Member creator) {
        return new MonthlyEvent(now, name, category, startDateTime, repetition, house, creator);
    }
}
