package com.roundtable.roundtable.domain.event;

import static com.roundtable.roundtable.domain.event.RepetitionType.DAILY;
import static com.roundtable.roundtable.domain.event.RepetitionType.MONTHLY;
import static com.roundtable.roundtable.domain.event.RepetitionType.WEEKLY;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.event.adjuster.DailyStartDateTimeAdjuster;
import com.roundtable.roundtable.domain.event.adjuster.MonthlyStartDateTimeAdjuster;
import com.roundtable.roundtable.domain.event.adjuster.StartDateTimeAdjuster;
import com.roundtable.roundtable.domain.event.adjuster.WeeklyStartDateTimeAdjuster;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull
    private LocalDateTime startDateTime;

    @Embedded
    private Repetition repetition;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event parentEvent;

    @Builder
    private Event(Long id, String name, Category category, LocalDateTime startDateTime, Repetition repetition,
                  House house,
                  Member creator, Event parentEvent) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.startDateTime = startDateTime;
        this.repetition = repetition;
        this.house = house;
        this.creator = creator;
        this.parentEvent = parentEvent;
    }

    public static Event oneTime(String name, Category category, LocalDateTime startDateTime,
                                House house,
                                Member creator) {
        return Event.builder()
                .name(name)
                .category(category)
                .startDateTime(startDateTime)
                .house(house)
                .creator(creator)
                .build();
    }

    public static Event repetition(LocalDate now, String name, Category category, LocalDateTime startDateTime,
                                   Repetition repetition,
                                   House house,
                                   Member creator) {
        StartDateTimeAdjuster startDateTimeAdjuster = createStartDateTimeAdjuster(now, startDateTime, repetition);
        return Event.builder()
                .name(name)
                .category(category)
                .startDateTime(startDateTimeAdjuster.adjustStartDateTime())
                .repetition(repetition)
                .house(house)
                .creator(creator)
                .build();
    }

    private static StartDateTimeAdjuster createStartDateTimeAdjuster(LocalDate now, LocalDateTime startDateTime,
                                                                     Repetition repetition) {
        if (repetition.getRepetitionType() == DAILY) {
            return new DailyStartDateTimeAdjuster(now, startDateTime);
        }
        if (repetition.getRepetitionType() == WEEKLY) {
            return new WeeklyStartDateTimeAdjuster(now, startDateTime, repetition.getDaysOfWeeks());
        }
        if (repetition.getRepetitionType() == MONTHLY) {
            return new MonthlyStartDateTimeAdjuster(now, startDateTime);
        }

        throw new IllegalArgumentException("반복 타입에 맞는 StartDateTimeAdjuster 가 존재하지 않습니다.");
    }

    public boolean isSameHouse(Member member) {
        return member.isSameHouse(this.house);
    }

    public Event createChild() {
        return Event.builder()
                .name(name)
                .category(category)
                .startDateTime(startDateTime)
                .house(house)
                .creator(creator)
                .startDateTime(startDateTime)
                .repetition(repetition)
                .parentEvent(this)
                .build();
    }

    public boolean isWeeklyEvent() {
        return repetition != null && repetition.isWeekly();
    }
}
