package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.sse.HouseSsePublisher;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.EventCompletionNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class EventCompletionNotificationAppender {
    private final MemberReader memberReader;
    private final EventDateTimeSlotRepository eventDateTimeSlotRepository;
    private final NotificationRepository notificationRepository;
    private final HouseSsePublisher houseSsePublisher;

    public void append(Long houseId, Long eventDateTimeSlotId, List<Long> participantIds) {
        EventDateTimeSlot eventDateTimeSlot = eventDateTimeSlotRepository.findById(eventDateTimeSlotId)
                .orElseThrow(NotFoundEntityException::new);
        List<Member> houseMembers = memberReader.findAllByHouseId(houseId);
        List<Member> participants = getParticipants(participantIds, houseMembers);
        List<Member> receivers = getReceivers(participantIds, houseMembers);

        List<EventCompletionNotification> eventCompletionNotifications = createEventCompletionNotifications(houseId,
                receivers, eventDateTimeSlot.getEvent(), participants);
        notificationRepository.saveAll(eventCompletionNotifications);

        if (!eventCompletionNotifications.isEmpty()) {
            houseSsePublisher.send(
                    houseId,
                    receivers.stream().map(Member::getId).toList(),
                    eventCompletionNotifications.get(0).toSseEvent()
            );
        }
    }

    private List<Member> getParticipants(List<Long> participantIds, List<Member> houseMembers) {
        return houseMembers.stream()
                .filter(houseMember -> participantIds.contains(houseMember.getId()))
                .toList();
    }

    private List<Member> getReceivers(List<Long> participantIds, List<Member> houseMembers) {
        return houseMembers.stream()
                .filter(houseMember -> !participantIds.contains(houseMember.getId()))
                .toList();
    }

    private List<EventCompletionNotification> createEventCompletionNotifications(Long houseId,
                                                                                 List<Member> receivers,
                                                                                 Event event,
                                                                                 List<Member> participants) {
        return receivers.stream()
                .map(receiver -> EventCompletionNotification.create(
                        receiver,
                        House.Id(houseId),
                        event.getId(),
                        event.getName(),
                        participants))
                .toList();
    }
}
