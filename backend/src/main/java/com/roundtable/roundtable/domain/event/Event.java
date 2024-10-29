package com.roundtable.roundtable.domain.event;

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
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

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
    private Event(Long id,
                  String name,
                  Category category,
                  LocalDateTime startDateTime,
                  Repetition repetition,
                  House house,
                  Event parentEvent,
                  Member creator) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.startDateTime = startDateTime;
        this.repetition = repetition;
        this.house = house;
        this.parentEvent = parentEvent;
        this.creator = creator;
    }

    public static Event oneTime(
            String name,
            Category category,
            LocalDateTime startDateTime,
            House house,
            Member creator
    ) {
        return Event.builder()
                .name(name)
                .category(category)
                .startDateTime(startDateTime)
                .house(house)
                .creator(creator)
                .build();
    }

    public static Event repetition(
            String name,
            Category category,
            LocalDateTime startDateTime,
            House house,
            Member creator,
            Repetition repetition
    ) {
        return Event.builder()
                .name(name)
                .category(category)
                .startDateTime(startDateTime)
                .house(house)
                .creator(creator)
                .repetition(repetition)
                .build();
    }
}
