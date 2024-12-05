package com.roundtable.roundtable.domain.event.repository;

import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static com.roundtable.roundtable.domain.event.QEvent.event;
import static com.roundtable.roundtable.domain.event.QEventDateTimeSlot.eventDateTimeSlot;
import static com.roundtable.roundtable.domain.event.QEventParticipant.eventParticipant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailDto;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailOfMemberDto;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDto;
import com.roundtable.roundtable.domain.event.dto.QEventDateTimeSlotDetailDto;
import com.roundtable.roundtable.domain.event.dto.QEventDateTimeSlotDetailOfMemberDto;
import com.roundtable.roundtable.domain.event.dto.QEventDateTimeSlotDto;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class EventDateTimeSlotQueryRepository {

    private static final String MEMBER_NAME_JOIN_PATTER = "GROUP_CONCAT(DISTINCT {0})";

    private final JPAQueryFactory queryFactory;

    public EventDateTimeSlotQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<EventDateTimeSlotDto> findEventDateTimeSlotBetweenDate(Long houseId,
                                                                       LocalDateTime startDateTime,
                                                                       LocalDateTime endDateTime) {
        return queryFactory
                .select(new QEventDateTimeSlotDto(
                        event.id,
                        eventDateTimeSlot.id,
                        event.name,
                        eventDateTimeSlot.isCompleted,
                        eventDateTimeSlot.startTime
                ))
                .from(eventDateTimeSlot)
                .join(eventDateTimeSlot.event, event)
                .where(
                        event.house.id.eq(houseId),
                        eventDateTimeSlot.startTime.goe(startDateTime),
                        eventDateTimeSlot.startTime.loe(endDateTime)
                )
                .fetch();
    }

    public List<EventDateTimeSlotDetailDto> findEventDateTimeSlotsByDate(
            Long houseId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        return queryFactory
                .select(new QEventDateTimeSlotDetailDto(
                        event.id,
                        eventDateTimeSlot.id,
                        event.name,
                        event.category,
                        eventDateTimeSlot.isCompleted,
                        eventDateTimeSlot.startTime,
                        stringTemplate(MEMBER_NAME_JOIN_PATTER, eventParticipant.participant.name)
                ))
                .from(eventDateTimeSlot)
                .join(eventDateTimeSlot.event, event)
                .join(eventParticipant).on(eventParticipant.event.eq(event))
                .join(eventParticipant.participant)
                .where(
                        event.house.id.eq(houseId),
                        eventDateTimeSlot.startTime.goe(startDateTime),
                        eventDateTimeSlot.startTime.loe(endDateTime)
                )
                .groupBy(event.id, eventDateTimeSlot.id)
                .fetch();
    }

    public List<EventDateTimeSlotDetailOfMemberDto> findEventDateTimeSlotsOfMemberByDate(
            Long houseId,
            Long memberId,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    ) {
        return queryFactory
                .select(new QEventDateTimeSlotDetailOfMemberDto(
                        event.id,
                        eventDateTimeSlot.id,
                        event.name,
                        event.category,
                        eventDateTimeSlot.isCompleted,
                        eventDateTimeSlot.startTime
                ))
                .from(eventDateTimeSlot)
                .join(eventDateTimeSlot.event, event)
                .join(eventParticipant).on(eventParticipant.event.eq(event))
                .where(
                        event.house.id.eq(houseId),
                        eventParticipant.participant.id.eq(memberId),
                        eventDateTimeSlot.startTime.goe(startDateTime),
                        eventDateTimeSlot.startTime.loe(endDateTime)
                )
                .groupBy(event.id, eventDateTimeSlot.id)
                .fetch();
    }
}
