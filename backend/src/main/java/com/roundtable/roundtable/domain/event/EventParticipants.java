package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import java.util.List;

public class EventParticipants {
    private final Event event;
    private final List<Member> participants;

    public EventParticipants(Event event, List<Member> participants, House house) {
        List<Member> sameHouseMembers = getSameHouseMembers(participants, house);
        if (participants.size() != sameHouseMembers.size()) {
            throw new IllegalArgumentException("같은 하우스에 속한 사용자만 참여할 수 있습니다.");
        }

        this.participants = participants;
        this.event = event;
    }

    private List<Member> getSameHouseMembers(List<Member> members, House house) {
        return members.stream()
                .filter(participant -> participant.isSameHouse(house))
                .toList();
    }

    public List<EventParticipant> getEventParticipants() {
        return participants.stream()
                .map(participant -> EventParticipant.of(event, participant))
                .toList();
    }
}
