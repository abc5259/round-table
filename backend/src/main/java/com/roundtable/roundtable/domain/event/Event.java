package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.schedule.Category;
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
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event parentEvent;

    @NotNull
    private LocalDateTime startDateTime;

    private RepetitionType repetitionType;

    private Integer repeatCycle;

    private LocalDate repeatedUntilDate;
}
