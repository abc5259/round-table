package com.roundtable.roundtable.domain.schedule;

import com.roundtable.roundtable.domain.member.Member;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

public class ScheduleManagers {

    private final Schedule schedule;
    private final List<Member> managers;
    private final Integer sequence;

    public ScheduleManagers(Schedule schedule, List<Member> managers, Integer sequence) {
        this.schedule = schedule;
        this.managers = managers;
        this.sequence = sequence;
    }

    public static ScheduleManagers of(Schedule schedule, List<ScheduleMember> scheduleMembers, List<ExtraScheduleMember> extraScheduleMembers) {
        List<Member> managers = Stream.concat(
                        scheduleMembers.stream().filter(ScheduleMember::isManager).map(ScheduleMember::getMember),
                        extraScheduleMembers.stream().map(ExtraScheduleMember::getMember)
                )
                .toList();

        if(managers.isEmpty() || scheduleMembers.isEmpty()) {
            throw new IllegalArgumentException("오늘의 스케줄 담당자를 찾을 수 없습니다.");
        }

        return new ScheduleManagers(schedule, managers, scheduleMembers.stream().findFirst().get().getSequence());
    }

    public ScheduleCompletion complete(Member member, LocalDate completeDate) {
        validateIsManager(member);
        schedule.complete();

        return ScheduleCompletion.create(schedule, completeDate, sequence);
    }

    private void validateIsManager(Member member) {
        boolean isManager = isManager(member);
        if(!isManager) {
            throw new IllegalStateException("스케줄을 담당하는 담당자가 아닙니다.");
        }
    }

    private boolean isManager(Member member) {
        return managers.stream().anyMatch(manager -> manager.isEqualId(member));
    }

    public List<ScheduleCompletionMember> toScheduleCompletionMembers(ScheduleCompletion scheduleCompletion) {
        return managers.stream()
                .map(manager -> new ScheduleCompletionMember(scheduleCompletion, manager))
                .toList();
    }
}
