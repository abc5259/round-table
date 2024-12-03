package com.roundtable.roundtable.business.notification;

import com.roundtable.roundtable.business.chore.event.ChoreCompleteEvent;
import com.roundtable.roundtable.business.delegation.event.CreateDelegationEvent;
import com.roundtable.roundtable.business.event.event.EventCompletionEvent;
import com.roundtable.roundtable.business.feedback.event.CreateFeedbackEvent;
import com.roundtable.roundtable.business.house.event.HouseCreatedEvent;
import com.roundtable.roundtable.business.notification.dto.CreateFeedbackNotification;
import com.roundtable.roundtable.business.notification.dto.CreateInviteNotification;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.errorcode.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationEventListener {

    private final InviteNotificationAppender inviteNotificationAppender;
    private final ChoreCompleteNotificationAppender choreCompleteNotificationAppender;
    private final FeedbackNotificationAppender feedbackNotificationAppender;
    private final EventCompletionNotificationAppender eventCompletionNotificationAppender;
    private final DelegationNotificationAppender delegationNotificationAppender;

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createInviteNotification(HouseCreatedEvent houseCreatedEvent) {
        try {
            inviteNotificationAppender.append(
                    new CreateInviteNotification(
                            houseCreatedEvent.appenderId(),
                            houseCreatedEvent.houseId(),
                            houseCreatedEvent.invitedEmails()
                    )
            );
        } catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[createInviteNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        } catch (RuntimeException e) {
            log.error("[createInviteNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createChoreCompleteNotification(ChoreCompleteEvent choreCompleteEvent) {
        try {
            choreCompleteNotificationAppender.append(
                    choreCompleteEvent.houseId(),
                    choreCompleteEvent.completedChoreId(),
                    choreCompleteEvent.completedMemberId()
            );
        } catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[createChoreCompleteNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        } catch (RuntimeException e) {
            log.error("[createChoreCompleteNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createEventCompletionNotification(EventCompletionEvent eventCompletionEvent) {
        try {
            eventCompletionNotificationAppender.append(
                    eventCompletionEvent.houseId(),
                    eventCompletionEvent.eventDateTimeSlotId(),
                    eventCompletionEvent.eventParticipantIds()
            );
        } catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[createEventCompletionNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(),
                    e);
        } catch (RuntimeException e) {
            log.error("[createEventCompletionNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createFeedbackNotification(CreateFeedbackEvent createFeedbackEvent) {
        try {
            feedbackNotificationAppender.append(
                    new CreateFeedbackNotification(
                            createFeedbackEvent.feedbackId(),
                            createFeedbackEvent.senderId(),
                            createFeedbackEvent.eventId(),
                            createFeedbackEvent.houseId()
                    )
            );
        } catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[createFeedbackNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        } catch (RuntimeException e) {
            log.error("[createFeedbackNotification 에러] - {}", e.getMessage(), e);
        }
    }

    @Transactional
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void createDelegationNotification(CreateDelegationEvent createDelegationEvent) {
        try {
            delegationNotificationAppender.append(
                    createDelegationEvent.houseId(),
                    createDelegationEvent.sender(),
                    createDelegationEvent.receiver(),
                    createDelegationEvent.scheduleId(),
                    createDelegationEvent.delegation()
            );
        } catch (CoreException e) {
            ErrorCode errorCode = e.getErrorCode();
            log.error("[createDelegationNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
        } catch (RuntimeException e) {
            log.error("[createDelegationNotification 에러] - {}", e.getMessage(), e);
        }
    }
//
//    @Transactional
//    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
//    @Async
//    public void updateDelegationNotification(UpdateDelegationEvent updateDelegationEvent) {
//        try {
//            delegationNotificationAppender.append(
//                    updateDelegationEvent.houseId(),
//                    updateDelegationEvent.delegation()
//            );
//        } catch (CoreException e) {
//            ErrorCode errorCode = e.getErrorCode();
//            log.error("[updateDelegationNotification 에러] - {} {}", errorCode.getMessage(), errorCode.getCode(), e);
//        } catch (RuntimeException e) {
//            log.error("[updateDelegationNotification 에러] - {}", e.getMessage(), e);
//        }
//    }
}
