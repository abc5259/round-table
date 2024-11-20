package com.roundtable.roundtable.business.event;

import static com.roundtable.roundtable.domain.event.RepetitionType.WEEKLY;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto.RepetitionDto;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDayOfWeek;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.repository.EventDayOfWeekRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepetitionEventService {

    private static final int MAX_TIME_SLOT_SIZE = 30;

    private final EventRepository eventRepository;
    private final EventDateTimeSlotAppender eventDateTimeSlotAppender;
    private final EventDayOfWeekRepository eventDayOfWeekRepository;

    @Transactional
    public void createEvent(CreateRepetitionEventDto createRepetitionEventDto, AuthMember authMember, LocalDate now) {
        RepetitionDto repetitionDto = createRepetitionEventDto.repetitionDto();
        Repetition repetition = repetitionDto.toRepetition();

        Event event = Event.repetition(
                now,
                createRepetitionEventDto.eventName(),
                createRepetitionEventDto.category(),
                createRepetitionEventDto.startDateTime(),
                repetition,
                House.Id(authMember.houseId()),
                Member.Id(authMember.memberId())
        );
        eventRepository.save(event);
        appendEventDateTimeSlots(repetitionDto, event);

        if (repetitionDto.repetitionType() == WEEKLY) {
            appendEventDayOfWeeks(repetitionDto, event);
        }
    }

    private void appendEventDateTimeSlots(RepetitionDto repetitionDto, Event event) {
        if (repetitionDto.repetitionType() == WEEKLY) {
            eventDateTimeSlotAppender.append(
                    event,
                    event.getStartDateTime(),
                    repetitionDto.getDays(),
                    MAX_TIME_SLOT_SIZE);
            return;
        }
        eventDateTimeSlotAppender.append(event, event.getStartDateTime(), MAX_TIME_SLOT_SIZE);
    }

    private void appendEventDayOfWeeks(RepetitionDto repetitionDto, Event event) {
        List<EventDayOfWeek> dayOfWeeks = repetitionDto.days().stream()
                .map(day -> new EventDayOfWeek(event, day))
                .toList();
        eventDayOfWeekRepository.saveAll(dayOfWeeks);
    }
}
