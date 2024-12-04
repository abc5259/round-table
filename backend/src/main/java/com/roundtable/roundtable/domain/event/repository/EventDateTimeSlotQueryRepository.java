package com.roundtable.roundtable.domain.event.repository;

import static com.querydsl.core.types.dsl.Expressions.stringTemplate;
import static com.roundtable.roundtable.domain.event.QEvent.event;
import static com.roundtable.roundtable.domain.event.QEventDateTimeSlot.eventDateTimeSlot;
import static com.roundtable.roundtable.domain.event.QEventParticipant.eventParticipant;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailDto;
import com.roundtable.roundtable.domain.event.dto.QEventDateTimeSlotDetailDto;
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

    public List<EventDateTimeSlotDetailDto> findEventDateTimeSlotsByDate(
            Long houseId,
            LocalDateTime dateTime
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
                        eventDateTimeSlot.startTime.eq(dateTime)
                )
                .groupBy(event.id, eventDateTimeSlot.id)
                .fetch();
    }
}
