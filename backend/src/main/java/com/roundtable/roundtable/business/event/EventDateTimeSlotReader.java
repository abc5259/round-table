package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailDto;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotQueryRepository;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.global.exception.CoreException.NotFoundEntityException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventDateTimeSlotReader {

    private final EventDateTimeSlotRepository eventDateTimeSlotRepository;
    private final EventDateTimeSlotQueryRepository eventDateTimeSlotQueryRepository;


    public EventDateTimeSlot readEventDateTimeSlotById(Long eventDateTimeSlotId) {
        return eventDateTimeSlotRepository.findById(eventDateTimeSlotId)
                .orElseThrow(NotFoundEntityException::new);
    }

    public List<EventDateTimeSlotDetailDto> readEventDateTimeSlotDetails(Long houseId, LocalDateTime startDateTime,
                                                                         LocalDateTime endDateTime) {
        return eventDateTimeSlotQueryRepository.findEventDateTimeSlotsByDate(houseId, startDateTime, endDateTime);
    }
}
