package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.notification.dto.CreateFeedbackNotification;
import com.roundtable.roundtable.business.sse.HouseSsePublisher;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class FeedbackNotificationAppender {

    private final MemberReader memberReader;
    private final NotificationRepository notificationRepository;
    private final EventRepository eventRepository;
    private final EventParticipantRepository eventParticipantRepository;
    private final HouseSsePublisher houseSsePublisher;

    public void append(CreateFeedbackNotification createFeedbackNotification) {
        Member sender = memberReader.findById(createFeedbackNotification.senderId());
        Event event = eventRepository.findById(createFeedbackNotification.eventId())
                .orElseThrow(NotFoundEntityException::new);
        List<EventParticipant> eventParticipants = eventParticipantRepository.findAllByEventId(
                createFeedbackNotification.eventId());

        List<FeedbackNotification> feedbackNotifications = FeedbackNotification.create(sender, eventParticipants, event,
                sender.getHouse(),
                createFeedbackNotification.feedbackId());

        notificationRepository.saveAll(feedbackNotifications);

        if (!feedbackNotifications.isEmpty()) {
            houseSsePublisher.send(createFeedbackNotification.houseId(), eventParticipants.stream().map(
                            EventParticipant::getParticipantId).toList(),
                    feedbackNotifications.get(0).toSseEvent());
        }
    }
}
