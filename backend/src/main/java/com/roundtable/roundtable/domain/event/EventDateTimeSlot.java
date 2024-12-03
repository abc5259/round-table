package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.member.Member;
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

    public void validateCreateFeedback(Member feedbackCreator) {
        if (!event.isSameHouse(feedbackCreator)) {
            throw new IllegalArgumentException("같은 하우스에 있는 이벤트에만 피드백을 할 수 있습니다.");
        }

        if (!isCompleted) {
            throw new IllegalStateException("완료되지 않은 스케줄에는 피드백을 보낼 수 없습니다.");
        }
    }

    public void complete() {
        if (isCompleted) {
            throw new IllegalStateException("이미 완료된 이벤트입니다.");
        }

        this.isCompleted = true;
    }

    public void complete(Member completeMember, EventParticipants eventParticipants) {
        validateCompleteEvent(completeMember, eventParticipants);
        this.isCompleted = true;
    }

    private void validateCompleteEvent(Member completeMember, EventParticipants eventParticipants) {
        if (!eventParticipants.containMemberId(completeMember)) {
            throw new IllegalArgumentException("이벤트 참가자만 완료가능합니다.");
        }

        if (isCompleted) {
            throw new IllegalStateException("이미 완료된 이벤트입니다.");
        }
    }

    public String getEventName() {
        return this.event.getName();
    }
}
