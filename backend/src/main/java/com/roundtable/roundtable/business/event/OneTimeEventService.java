package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateEventDto;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OneTimeEventService {

    private final MemberReader memberReader;
    private final EventRepository eventRepository;
    private final EventDateTimeSlotRepository eventDateTimeSlotRepository;
    private final EventParticipantRepository eventParticipantRepository;

    @Transactional
    public void createEvent(CreateEventDto createEventDto, AuthMember authMember) {

        List<Member> members = memberReader.findAllByIdOrThrow(createEventDto.participantIds());
        House house = House.Id(authMember.houseId());

        Event event = Event.oneTime(
                createEventDto.eventName(),
                createEventDto.category(),
                createEventDto.startDateTime(),
                house,
                Member.Id(authMember.memberId())
        );
        EventDateTimeSlot eventTimeSlot = EventDateTimeSlot.of(event, createEventDto.startDateTime());
        List<EventParticipant> eventParticipants = EventParticipant.listOf(event, members, house);

        eventRepository.save(event);
        eventDateTimeSlotRepository.save(eventTimeSlot);
        eventParticipantRepository.saveAll(eventParticipants);
    }
}
