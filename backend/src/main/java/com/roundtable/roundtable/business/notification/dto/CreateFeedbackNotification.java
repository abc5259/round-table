package com.roundtable.roundtable.business.notification.dto;

public record CreateFeedbackNotification(
        Long feedbackId,
        Long senderId,
        Long eventId,
        Long houseId
) {
}
