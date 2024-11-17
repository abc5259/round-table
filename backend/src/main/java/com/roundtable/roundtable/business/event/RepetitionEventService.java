package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto.RepetitionDto;
import com.roundtable.roundtable.domain.event.DailyEvent;
import com.roundtable.roundtable.domain.event.Day;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.MonthlyEvent;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.RepetitionType;
import com.roundtable.roundtable.domain.event.WeeklyEvent;
import com.roundtable.roundtable.domain.event.repository.EventDayOfWeekRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.global.exception.CoreException;
import com.roundtable.roundtable.global.exception.errorcode.EventErrorCode;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RepetitionEventService {

    private final EventRepository eventRepository;
    private final EventDayOfWeekRepository eventDayOfWeekRepository;

    @Transactional
    public void createEvent(CreateRepetitionEventDto createRepetitionEventDto, AuthMember authMember) {

        RepetitionDto repetitionDto = createRepetitionEventDto.repetitionDto();

        Repetition repetition = Repetition.of(
                repetitionDto.repetitionType(),
                repetitionDto.repeatCycle(),
                repetitionDto.repeatedUntilDate());

        Event event = createEvent(createRepetitionEventDto, authMember, repetitionDto, repetition);
        //TODO 타임 슬롯 만들어야함

        eventRepository.save(event);
    }

    private Event createEvent(CreateRepetitionEventDto createRepetitionEventDto,
                              AuthMember authMember,
                              RepetitionDto repetitionDto,
                              Repetition repetition) {
        if (repetitionDto.repetitionType() == RepetitionType.DAILY) {
            return DailyEvent.from(
                    LocalDate.now(),
                    createRepetitionEventDto.eventName(),
                    createRepetitionEventDto.category(),
                    createRepetitionEventDto.startDateTime(),
                    repetition,
                    House.Id(authMember.houseId()),
                    Member.Id(authMember.memberId()));
        }
        if (repetitionDto.repetitionType() == RepetitionType.WEEKLY) {
            return WeeklyEvent.from(
                    LocalDate.now(),
                    createRepetitionEventDto.eventName(),
                    createRepetitionEventDto.category(),
                    createRepetitionEventDto.startDateTime(),
                    repetition,
                    House.Id(authMember.houseId()),
                    Member.Id(authMember.memberId()),
                    createRepetitionEventDto.repetitionDto().days().stream().map(Day::toDayOfWeek).toList());

        }
        if (repetitionDto.repetitionType() == RepetitionType.MONTHLY) {
            return MonthlyEvent.from(
                    LocalDate.now(),
                    createRepetitionEventDto.eventName(),
                    createRepetitionEventDto.category(),
                    createRepetitionEventDto.startDateTime(),
                    repetition,
                    House.Id(authMember.houseId()),
                    Member.Id(authMember.memberId()));
        }

        throw new CoreException(EventErrorCode.UNSUPPORTED_REPETITION_TYPE);
    }
}