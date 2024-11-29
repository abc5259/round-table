package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public EventParticipants(List<EventParticipant> eventParticipants) {
        Map<Event, List<Member>> eventToParticipantsMap = eventParticipants.stream()
                .collect(Collectors.groupingBy(
                        EventParticipant::getEvent,
                        Collectors.mapping(EventParticipant::getParticipant, Collectors.toList()) // Value: List<Member>
                ));
        if (eventToParticipantsMap.size() != 1) {
            throw new IllegalArgumentException("모든 EventParticipant는 동일한 Event에 속해야 합니다.");
        }

        Map.Entry<Event, List<Member>> entry = eventToParticipantsMap.entrySet().iterator().next();
        this.event = entry.getKey();
        this.participants = entry.getValue();
    }

    public List<EventParticipant> getEventParticipants() {
        return participants.stream()
                .map(participant -> EventParticipant.of(event, participant))
                .toList();
    }

    public void validateCreateFeedback(Member sender) {
        participants.stream()
                .filter(sender::isEqualId)
                .findFirst()
                .ifPresent(eventParticipant -> {
                    throw new IllegalArgumentException("이벤트에 참여하지 않는 사람만 피드백이 가능합니다.");
                });
    }

    private List<Member> getSameHouseMembers(List<Member> members, House house) {
        return members.stream()
                .filter(participant -> participant.isSameHouse(house))
                .toList();
    }
}
