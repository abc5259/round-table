package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateEventDto;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipants;
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
    public Long createEvent(CreateEventDto createOneTimeEventDto, AuthMember authMember) {

        List<Member> members = memberReader.findAllByIdOrThrow(createOneTimeEventDto.participantIds());
        House house = House.Id(authMember.houseId());

        Event event = Event.oneTime(
                createOneTimeEventDto.eventName(),
                createOneTimeEventDto.category(),
                createOneTimeEventDto.startDateTime(),
                house,
                Member.Id(authMember.memberId())
        );
        EventDateTimeSlot eventTimeSlot = EventDateTimeSlot.of(event, createOneTimeEventDto.startDateTime());
        EventParticipants eventParticipants = new EventParticipants(event, members, house);

        eventRepository.save(event);
        eventDateTimeSlotRepository.save(eventTimeSlot);
        eventParticipantRepository.saveAll(eventParticipants.getEventParticipants());
        return event.getId();
    }
}
