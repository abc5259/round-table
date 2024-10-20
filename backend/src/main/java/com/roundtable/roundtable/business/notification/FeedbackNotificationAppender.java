package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.business.sse.HouseSsePublisher;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.FeedbackNotification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleCompletionRepository;
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
    private final ScheduleCompletionRepository scheduleCompletionRepository;
    private final ScheduleCompletionMemberRepository scheduleCompletionMemberRepository;
    private final HouseSsePublisher houseSsePublisher;

    public void append(Long feedbackId, Long houseId, Long scheduleCompletionId, Long senderId) {
        Member sender = memberReader.findById(senderId);
        ScheduleCompletion scheduleCompletion = scheduleCompletionRepository.findWithScheduleById(scheduleCompletionId)
                .orElseThrow(NotFoundEntityException::new);
        List<Member> receivers = scheduleCompletionMemberRepository.findMembersByScheduleCompletionId(scheduleCompletionId);

        List<FeedbackNotification> feedbackNotifications = FeedbackNotification.create(
                sender,
                receivers,
                House.Id(houseId),
                feedbackId,
                scheduleCompletion
        );
        notificationRepository.saveAll(feedbackNotifications);

        if(!feedbackNotifications.isEmpty()) {
            houseSsePublisher.send(houseId, receivers.stream().map(Member::getId).toList(), feedbackNotifications.get(0).toSseEvent());
        }
    }
}
