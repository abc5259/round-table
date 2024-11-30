package com.roundtable.roundtable.business.event.event;

import java.util.List;

public record EventCompletionEvent(
        Long houseId,
        Long eventDateTimeSlotId,
        List<Long> eventParticipantIds
) {
}
