package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CompleteEventDto;
import com.roundtable.roundtable.business.event.dto.CreateEventDto;
import com.roundtable.roundtable.business.event.event.EventCompletionEvent;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipants;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final OneTimeEventService oneTimeEventService;
    private final RepetitionEventService repetitionEventService;
    private final EventDateTimeSlotRepository eventDateTimeSlotRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long createEvent(CreateEventDto createEventDto, AuthMember authMember) {
        if (createEventDto.isRepetitionEvent()) {
            return oneTimeEventService.createEvent(createEventDto, authMember);
        }

        return repetitionEventService.createEvent(createEventDto, authMember, LocalDate.now());
    }

    @Transactional
    public void completeEvent(CompleteEventDto completeEventDto, AuthMember authMember) {
        EventDateTimeSlot eventDateTimeSlot = eventDateTimeSlotRepository.findById(
                completeEventDto.eventDateTimeSlotId()).orElseThrow(
                NotFoundEntityException::new);
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
}
