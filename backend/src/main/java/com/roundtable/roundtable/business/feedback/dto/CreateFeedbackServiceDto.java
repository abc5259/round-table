package com.roundtable.roundtable.business.feedback.dto;

import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipants;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.member.Member;
import java.util.List;

public record CreateFeedbackServiceDto(
        Emoji emoji,
        String message,
        Long senderId,
        Long eventId,
        Long eventDateTimeSlotId,
        List<Integer> predefinedFeedbackIds
) {

    public CreateFeedback toCreateFeedback(Member sender, EventDateTimeSlot eventDateTimeSlot,
                                           EventParticipants eventParticipants) {
        return new CreateFeedback(
                emoji,
                message,
                sender,
                eventDateTimeSlot,
                predefinedFeedbackIds,
                eventParticipants
        );
    }
}
