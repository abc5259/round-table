package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member participant;

    @Builder
    private EventParticipant(Long id, Event event, Member participant) {
        this.id = id;
        this.event = event;
        this.participant = participant;
    }

    public static EventParticipant of(Event event, Member participant) {
        return EventParticipant.builder()
                .event(event)
                .participant(participant)
                .build();
    }
}
