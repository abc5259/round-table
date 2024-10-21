package com.roundtable.roundtable.business.schedule;

import com.roundtable.roundtable.domain.schedule.ExtraScheduleMember;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleManagers;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.repository.ExtraScheduleMemberRepository;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleMemberRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScheduleManagerReader {

    private final ScheduleMemberRepository scheduleMemberRepository;
    private final ExtraScheduleMemberRepository extraScheduleMemberRepository;

    public ScheduleManagers readScheduleManagersByDate(Schedule schedule, LocalDate date) {
        List<ScheduleMember> scheduleMembers = scheduleMemberRepository.findByScheduleId(schedule.getId());
        List<ExtraScheduleMember> extraScheduleMembers = extraScheduleMemberRepository.findByScheduleIdAndAssignedDate(schedule.getId(), date);

        return ScheduleManagers.of(schedule, scheduleMembers, extraScheduleMembers);
    }
}
