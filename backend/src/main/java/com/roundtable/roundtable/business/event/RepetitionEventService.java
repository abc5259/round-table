package com.roundtable.roundtable.business.event;

import static com.roundtable.roundtable.domain.event.RepetitionType.WEEKLY;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto.RepetitionDto;
import com.roundtable.roundtable.business.member.MemberReader;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDayOfWeek;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.repository.EventDayOfWeekRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
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

    private final MemberReader memberReader;
    private final EventRepository eventRepository;
    private final EventDateTimeSlotAppender eventDateTimeSlotAppender;
    private final EventDayOfWeekRepository eventDayOfWeekRepository;
    private final EventParticipantRepository eventParticipantRepository;

    @Transactional
    public void createEvent(CreateRepetitionEventDto createRepetitionEventDto, AuthMember authMember, LocalDate now) {
        List<Member> members = memberReader.findAllByIdOrThrow(createRepetitionEventDto.participantIds());
        RepetitionDto repetitionDto = createRepetitionEventDto.repetitionDto();
        Repetition repetition = repetitionDto.toRepetition();
        House house = House.Id(authMember.houseId());

        Event event = Event.repetition(
                now,
                createRepetitionEventDto.eventName(),
                createRepetitionEventDto.category(),
                createRepetitionEventDto.startDateTime(),
                repetition,
                house,
                Member.Id(authMember.memberId())
        );
        List<EventParticipant> eventParticipants = EventParticipant.listOf(event, members, house);

        eventRepository.save(event);
        eventParticipantRepository.saveAll(eventParticipants);
        eventDateTimeSlotAppender.append(event, event.getStartDateTime(), MAX_TIME_SLOT_SIZE);
        if (repetitionDto.repetitionType() == WEEKLY) {
            appendEventDayOfWeeks(repetitionDto, event);
        }
    }

    private void appendEventDayOfWeeks(RepetitionDto repetitionDto, Event event) {
        List<EventDayOfWeek> dayOfWeeks = repetitionDto.days().stream()
                .map(day -> new EventDayOfWeek(event, day))
                .toList();
        eventDayOfWeekRepository.saveAll(dayOfWeeks);
    }
}
