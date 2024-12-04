package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateEventDto;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final OneTimeEventService oneTimeEventService;
    private final RepetitionEventService repetitionEventService;

    @Transactional
    public Long createEvent(CreateEventDto createEventDto, AuthMember authMember) {
        if (createEventDto.isRepetitionEvent()) {
            return oneTimeEventService.createEvent(createEventDto, authMember);
        }

        return repetitionEventService.createEvent(createEventDto, authMember, LocalDate.now());
    }
}
