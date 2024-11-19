package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    protected String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    protected Category category;

    @NotNull
    protected LocalDateTime startDateTime;

    @Embedded
    protected Repetition repetition;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected House house;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    protected Member creator;

    @ManyToOne(fetch = FetchType.LAZY)
    protected Event parentEvent;

    protected Event(String name,
                    Category category,
                    LocalDateTime startDateTime,
                    Repetition repetition,
                    House house,
                    Event parentEvent,
                    Member creator) {
        this.name = name;
        this.category = category;
        this.startDateTime = startDateTime;
        this.repetition = repetition;
        this.house = house;
        this.parentEvent = parentEvent;
        this.creator = creator;
    }

    protected Event(String name,
                    Category category,
                    LocalDateTime startDateTime,
                    Repetition repetition,
                    House house,
                    Member creator) {
        this.name = name;
        this.category = category;
        this.startDateTime = startDateTime;
        this.repetition = repetition;
        this.house = house;
        this.creator = creator;
    }
}
