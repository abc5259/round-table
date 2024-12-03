package com.roundtable.roundtable.business.delegation;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.delegation.dto.CreateDelegationDto;
import com.roundtable.roundtable.business.delegation.event.CreateDelegationEvent;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationRepository;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventDayOfWeek;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.EventParticipants;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventDayOfWeekRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import com.roundtable.roundtable.global.exception.DelegationException;
import com.roundtable.roundtable.global.exception.errorcode.DelegationErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DelegationService {
    private final MemberReader memberReader;
    private final DelegationRepository delegationRepository;
    private final EventRepository eventRepository;
    private final EventDateTimeSlotRepository eventDateTimeSlotRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final EventDayOfWeekRepository eventDayOfWeekRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public Long createDelegation(Long houseId, CreateDelegationDto createDelegationDto) {
        validateAlreadyExistDelegation(createDelegationDto);

        Member sender = memberReader.findById(createDelegationDto.senderId());
        Member receiver = memberReader.findById(createDelegationDto.receiverId());
        EventDateTimeSlot eventDateTimeSlot = eventDateTimeSlotRepository.findById(
                createDelegationDto.eventDateTimeSlotId()).orElseThrow(
                NotFoundEntityException::new);
        List<EventParticipant> eventParticipants = eventParticipantRepository.findAllByEvent(
                eventDateTimeSlot.getEvent());

        Delegation delegation = Delegation.create(
                eventParticipants,
                createDelegationDto.message(),
                eventDateTimeSlot,
                sender,
                receiver,
                createDelegationDto.delegationDate()
        );
        delegationRepository.save(delegation);
        publisher.publishEvent(
                new CreateDelegationEvent(
                        houseId,
                        sender,
                        receiver,
                        createDelegationDto.eventDateTimeSlotId(),
                        delegation
                ));

        return delegation.getId();
    }

    @Transactional
    public void approve(Long delegationId, Long eventId, AuthMember authMember) {
        Delegation delegation = delegationRepository.findById(delegationId).orElseThrow(NotFoundEntityException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundEntityException::new);
        EventParticipants eventParticipants = new EventParticipants(eventParticipantRepository.findAllByEvent(event));
        List<EventDayOfWeek> eventDayOfWeeks = eventDayOfWeekRepository.findByEvent(event);

        Event newEvent = event.createChild();
        delegation.approve(newEvent, authMember.memberId(), LocalDate.now());
        List<EventParticipant> newEventParticipants = eventParticipants.createChangedParticipants(
                newEvent, delegation.getSender(), delegation.getReceiver());
        List<EventDayOfWeek> newDayOfWeeks = createNewEventDayOfWeeks(eventDayOfWeeks, newEvent);

        eventRepository.save(event);
        eventParticipantRepository.saveAll(newEventParticipants);
        eventDayOfWeekRepository.saveAll(newDayOfWeeks);
    }

    private static List<EventDayOfWeek> createNewEventDayOfWeeks(List<EventDayOfWeek> eventDayOfWeeks, Event newEvent) {
        return eventDayOfWeeks.stream()
                .map(eventDayOfWeek -> new EventDayOfWeek(newEvent, eventDayOfWeek.getDayOfWeek())).toList();
    }


    private void validateAlreadyExistDelegation(CreateDelegationDto createDelegationDto) {
        if (delegationRepository.existsByEventDateTimeSlotIdAndSenderIdAndDelegationDate(
                createDelegationDto.eventDateTimeSlotId(),
                createDelegationDto.senderId(),
                createDelegationDto.delegationDate())
        ) {
            throw new DelegationException(DelegationErrorCode.ALREADY_EXIST_DELEGATION);
        }
    }
}
