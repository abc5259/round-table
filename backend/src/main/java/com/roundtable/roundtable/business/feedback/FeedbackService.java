package com.roundtable.roundtable.business.feedback;

import com.roundtable.roundtable.business.feedback.dto.CreateFeedbackServiceDto;
import com.roundtable.roundtable.business.feedback.event.CreateFeedbackEvent;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.EventParticipants;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackAppender feedbackAppender;
    private final MemberReader memberReader;
    private final EventParticipantRepository eventParticipantRepository;
    private final EventDateTimeSlotRepository eventDateTimeSlotRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Long createFeedback(CreateFeedbackServiceDto createFeedbackServiceDto, Long houseId) {
        Member sender = memberReader.findById(createFeedbackServiceDto.senderId());
        EventDateTimeSlot eventDateTimeSlot = eventDateTimeSlotRepository.findById(
                createFeedbackServiceDto.eventDateTimeSlotId()).orElseThrow(
                NotFoundEntityException::new);
        List<EventParticipant> eventParticipants = eventParticipantRepository.findAllByEventId(
                createFeedbackServiceDto.eventId());

        Feedback feedback = feedbackAppender.append(
                createFeedbackServiceDto.toCreateFeedback(sender, eventDateTimeSlot,
                        new EventParticipants(eventParticipants)));

        applicationEventPublisher.publishEvent(
                new CreateFeedbackEvent(
                        feedback.getId(),
                        createFeedbackServiceDto.senderId(),
                        createFeedbackServiceDto.eventId(),
                        houseId
                )
        );

        return feedback.getId();
    }
}
