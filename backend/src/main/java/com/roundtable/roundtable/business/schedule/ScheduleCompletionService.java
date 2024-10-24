package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.schedule.dto.ScheduleCompletionEvent;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletionMember;
import com.roundtable.roundtable.domain.schedule.ScheduleManagers;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleCompletionMemberRepository;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleDayRepository;
import com.roundtable.roundtable.global.exception.ScheduleException;
import com.roundtable.roundtable.global.exception.errorcode.ScheduleErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleCompletionService {

    private final ScheduleDayRepository scheduleDayRepository;
    private final ScheduleCompletionRepository scheduleCompletionRepository;
    private final ScheduleCompletionMemberRepository scheduleCompletionMemberRepository;
    private final ScheduleManagerReader scheduleManagerReader;
    private final ScheduleReader scheduleReader;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void complete(Long scheduleId, AuthMember member, LocalDate now) {
        Schedule schedule = scheduleReader.findById(scheduleId);
        validateCompletionSchedule(scheduleId, now);

        ScheduleManagers scheduleManagers = scheduleManagerReader.readScheduleManagersByDate(schedule, now);
        ScheduleCompletion scheduleCompletion = scheduleManagers.complete(Member.Id(member.memberId()), now);

        scheduleCompletionRepository.save(scheduleCompletion);
        List<ScheduleCompletionMember> scheduleCompletionMembers = appendScheduleCompletionMember(scheduleManagers,
                scheduleCompletion);

        applicationEventPublisher.publishEvent(new ScheduleCompletionEvent(member.houseId(), scheduleId,
                scheduleCompletionMembers.stream().map(ScheduleCompletionMember::getMemberId).toList()));
    }

    private void validateCompletionSchedule(Long scheduleId, LocalDate completionDate) {
        if (!scheduleDayRepository.existsByScheduleIdAndDayOfWeek(scheduleId,
                Day.forDayOfWeek(completionDate.getDayOfWeek()))) {
            throw new ScheduleException(ScheduleErrorCode.NOT_TODAY_SCHEDULE);
        }
        if (scheduleCompletionRepository.existsByScheduleIdAndCompletionDate(scheduleId, completionDate)) {
            throw new ScheduleException(ScheduleErrorCode.ALREADY_COMPLETION_SCHEDULE);
        }
    }

    private List<ScheduleCompletionMember> appendScheduleCompletionMember(ScheduleManagers scheduleManagers,
                                                                          ScheduleCompletion scheduleCompletion) {
        List<ScheduleCompletionMember> scheduleCompletionMembers = scheduleManagers.toScheduleCompletionMembers(
                scheduleCompletion);
        return scheduleCompletionMemberRepository.saveAll(scheduleCompletionMembers);
    }
}
