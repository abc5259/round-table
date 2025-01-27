package com.roundtable.roundtable.domain.schedule.repository;

import static com.roundtable.roundtable.domain.member.QMember.*;
import static com.roundtable.roundtable.domain.schedule.QSchedule.schedule;
import static com.roundtable.roundtable.domain.schedule.QScheduleMember.*;
import static java.util.stream.Collectors.*;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.dto.QScheduleMemberDetailDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleIdDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleMemberDetailDto;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleMemberQueryRepository {
    private final JPAQueryFactory queryFactory;

    public ScheduleMemberQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ScheduleMemberDetailDto> findScheduleMemberDetail(Long scheduleId) {
        return queryFactory
                .select(new QScheduleMemberDetailDto(
                        scheduleMember.member.id,
                        scheduleMember.member.name,
                        scheduleMember.sequence
                ))
                .from(scheduleMember)
                .join(scheduleMember.member, member)
                .where(scheduleMember.schedule.eq(Schedule.Id(scheduleId)))
                .fetch();
    }

    public Map<ScheduleIdDto, List<Member>> findAllocators(List<Schedule> schedules) {
        List<Tuple> results = queryFactory
                .select(schedule.id, scheduleMember.member.id)
                .from(scheduleMember)
                .join(scheduleMember.member, member)
                .join(scheduleMember.schedule, schedule)
                .where(schedule.in(schedules)
                        .and(schedule.sequence.eq(scheduleMember.sequence))
                )
                .fetch();

        return results.stream()
                .collect(
                        groupingBy(
                                result -> new ScheduleIdDto(result.get(schedule.id)),
                                mapping(result -> Member.Id(result.get(scheduleMember.member.id)), toList())
                        )
                );
    }

}
