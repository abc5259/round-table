package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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

    public static List<EventParticipant> listOf(Event event, List<Member> participants, House house) {
        List<Member> sameHouseMembers = getSameHouseMembers(participants, house);
        if (participants.size() != sameHouseMembers.size()) {
            throw new IllegalArgumentException("같은 하우스에 속한 사용자만 참여할 수 있습니다.");
        }

        return participants.stream()
                .map(participant -> EventParticipant.of(event, participant))
                .toList();
    }

    private static List<Member> getSameHouseMembers(List<Member> members, House house) {
        return members.stream()
                .filter(participant -> participant.isSameHouse(house))
                .toList();
    }
}
