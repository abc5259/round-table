package com.roundtable.roundtable.domain.schedule.repository;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.roundtable.roundtable.domain.schedule.QExtraScheduleMember.*;
import static com.roundtable.roundtable.domain.schedule.QSchedule.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleCompletion.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleDay.*;
import static com.roundtable.roundtable.domain.schedule.QScheduleMember.*;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.common.CursorPagination;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.dto.QScheduleDto;
import com.roundtable.roundtable.domain.schedule.dto.QScheduleOfMemberDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleDto;
import com.roundtable.roundtable.domain.schedule.dto.ScheduleOfMemberDto;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleQueryRepository {

    private static final String MEMBER_NAME_JOIN_PATTER = "GROUP_CONCAT(DISTINCT {0})";
    private static final NumberExpression<Integer> sequenceCondition = new CaseBuilder()
            .when(scheduleCompletion.isNull())
            .then(schedule.sequence)
            .otherwise(scheduleCompletion.sequence);

    private final JPAQueryFactory queryFactory;

    public ScheduleQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<ScheduleDto> findSchedulesByDate(Long houseId, LocalDate now, CursorPagination cursorPagination) {

        return queryFactory
                .select(new QScheduleDto(
                        schedule.id,
                        schedule.name,
                        schedule.category,
                        scheduleCompletion.id.isNotNull(),
                        schedule.startTime,
                        stringTemplate(MEMBER_NAME_JOIN_PATTER, scheduleMember.member.name),
                        stringTemplate(MEMBER_NAME_JOIN_PATTER, extraScheduleMember.member.name)
                ))
                .from(schedule)
                .leftJoin(scheduleCompletion).on(scheduleIdEq(scheduleCompletion.schedule.id).and(scheduleCompletionDateEq(now)))
                .join(scheduleDay).on(scheduleIdEq(scheduleDay.schedule.id).and(scheduleDayEq(now)))
                .join(scheduleMember).on(scheduleIdEq(scheduleMember.schedule.id).and(scheduleMemberSequenceEq(sequenceCondition)))
                .join(scheduleMember.member)
                .leftJoin(extraScheduleMember).on(scheduleIdEq(extraScheduleMember.schedule.id))
                .leftJoin(extraScheduleMember.member)
                .where(
                        scheduleHouseIdEq(houseId),
                        scheduleIdGt(cursorPagination.lastId())
                )
                .groupBy(schedule.id, scheduleCompletion.id)
                .orderBy(schedule.id.asc())
                .limit(cursorPagination.limit())
                .fetch();
    }

    public List<ScheduleOfMemberDto> findSchedulesByDateAndMemberId(Long houseId, LocalDate now, Long memberId, CursorPagination cursorPagination) {

        return queryFactory
                .select(new QScheduleOfMemberDto(
                        schedule.id,
                        schedule.name,
                        schedule.category,
                        scheduleCompletion.id.isNotNull(),
                        schedule.startTime.max()
                ))
                .from(schedule)
                .leftJoin(scheduleCompletion).on(scheduleIdEq(scheduleCompletion.schedule.id).and(scheduleCompletionDateEq(now)))
                .join(scheduleDay).on(scheduleIdEq(scheduleDay.schedule.id).and(scheduleDayEq(now)))
                .join(scheduleMember).on(scheduleIdEq(scheduleMember.schedule.id).and(scheduleMemberSequenceEq(sequenceCondition)))
                .join(scheduleMember.member)
                .leftJoin(extraScheduleMember).on(scheduleIdEq(extraScheduleMember.schedule.id))
                .leftJoin(extraScheduleMember.member)
                .where(
                        scheduleHouseIdEq(houseId),
                        scheduleIdGt(cursorPagination.lastId()),
                        scheduleMemberIdEq(memberId).or(extraMemberIdEq(memberId))
                )
                .groupBy(schedule.id, scheduleCompletion.id)
                .orderBy(schedule.id.asc())
                .limit(cursorPagination.limit())
                .fetch();
    }

    private BooleanExpression extraMemberIdEq(Long memberId) {
        return extraScheduleMember.member.id.eq(memberId);
    }

    private BooleanExpression scheduleMemberIdEq(Long memberId) {
        return scheduleMember.member.id.eq(memberId);
    }

    private BooleanExpression scheduleIdGt(Long id) {
        return schedule.id.gt(id);
    }

    private BooleanExpression scheduleHouseIdEq(Long houseId) {
        return schedule.house.id.eq(houseId);
    }

    private BooleanExpression scheduleIdEq(NumberPath<Long> id) {
        return schedule.id.eq(id);
    }

    private BooleanExpression scheduleCompletionDateEq(LocalDate now) {
        return scheduleCompletion.completionDate.eq(now);
    }

    private BooleanExpression scheduleDayEq(LocalDate now) {
        return scheduleDay.dayOfWeek.eq(Day.forDayOfWeek(now.getDayOfWeek()));
    }

    private BooleanExpression scheduleMemberSequenceEq(NumberExpression<Integer> sequenceCondition) {
        return scheduleMember.sequence.eq(sequenceCondition);
    }
}
