package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CompleteEventDto;
import com.roundtable.roundtable.business.event.event.EventCompletionEvent;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipants;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailDto;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailOfMemberDto;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDto;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.member.Member;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventDateTimeSlotService {

    private final EventDateTimeSlotReader eventDateTimeSlotReader;
    private final EventParticipantRepository eventParticipantRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void completeEvent(CompleteEventDto completeEventDto, AuthMember authMember) {
        EventDateTimeSlot eventDateTimeSlot = eventDateTimeSlotReader.readEventDateTimeSlotById(
                completeEventDto.eventDateTimeSlotId());
        EventParticipants eventParticipants = new EventParticipants(
                eventParticipantRepository.findAllByEvent(eventDateTimeSlot.getEvent())
        );

        eventDateTimeSlot.complete(Member.Id(authMember.memberId()), eventParticipants);

        applicationEventPublisher.publishEvent(new EventCompletionEvent(
                authMember.houseId(),
                completeEventDto.eventDateTimeSlotId(),
                eventParticipants.toParticipantIds()
        ));
    }

    public List<EventDateTimeSlotDetailDto> findEventDateTimeSlotDetailsByDate(AuthMember authMember,
                                                                               LocalDate date) {
        return eventDateTimeSlotReader.readEventDateTimeSlotDetailsByDate(
                authMember.houseId(),
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX));
    }

    public List<EventDateTimeSlotDetailOfMemberDto> findEventDateTimeSlotDetailsOfMemberIdByDate(AuthMember authMember,
                                                                                                 LocalDate date) {
        return eventDateTimeSlotReader.readEventDateTimeSlotDetailsOfMemberByDate(
                authMember.houseId(),
                authMember.memberId(),
                date.atStartOfDay(),
                date.atTime(LocalTime.MAX));
    }

    public List<EventDateTimeSlotDto> findEventDateTimeSlotsBetweenDate(AuthMember authMember,
                                                                        LocalDate startDate,
                                                                        LocalDate endDate) {
        return eventDateTimeSlotReader.readEventDateTimeSlotsBetweenDate(
                authMember.memberId(),
                startDate,
                endDate);
    }
}
