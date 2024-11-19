package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.common.BaseEntity;
import jakarta.persistence.Entity;
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
public class EventDateTimeSlot extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private boolean isCompleted;

    @NotNull
    private boolean isSkipped;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @Builder
    private EventDateTimeSlot(Long id, LocalDateTime startTime, boolean isCompleted, boolean isSkipped, Event event) {
        this.id = id;
        this.startTime = startTime;
        this.isCompleted = isCompleted;
        this.isSkipped = isSkipped;
        this.event = event;
    }

    public static EventDateTimeSlot of(Event event, LocalDateTime startTime) {
        return EventDateTimeSlot.builder()
                .event(event)
                .startTime(startTime)
                .build();
    }
}
