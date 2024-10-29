package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateEventDto;
import com.roundtable.roundtable.business.event.dto.CreateEventDto.RepetitionDto;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.repository.EventDayOfWeekRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepetitionEventService {

    private final EventRepository eventRepository;
    private final EventDayOfWeekRepository eventDayOfWeekRepository;

    @Transactional
    public void createEvent(CreateEventDto createEventDto, AuthMember authMember) {

        Event event;
        if (createEventDto.repetitionDto() == null) {
            event = Event.oneTime(
                    createEventDto.eventName(),
                    createEventDto.category(),
                    createEventDto.startDateTime(),
                    House.Id(authMember.houseId()),
                    Member.Id(authMember.memberId())
            );
        }

        RepetitionDto repetitionDto = createEventDto.repetitionDto();

        Repetition repetition = Repetition.of(
                repetitionDto.repetitionType(),
                repetitionDto.repeatCycle(),
                repetitionDto.repeatedUntilDate());

        event = Event.repetition(
                createEventDto.eventName(),
                createEventDto.category(),
                createEventDto.startDateTime(),
                House.Id(authMember.houseId()),
                Member.Id(authMember.memberId()),
                repetition
        );


    }
}
