package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.domain.event.Days;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.RecurringEventDateTimeCalculator;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotBulkRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class EventDateTimeSlotAppender {

    private final RecurringEventDateTimeCalculator recurringEventDateTimeCalculator;
    private final EventDateTimeSlotBulkRepository eventDateTimeSlotBulkRepository;

    public void append(Event event, LocalDateTime startDateTime, int count) {
        Repetition repetition = event.getRepetition();
        List<LocalDateTime> dateTimes = recurringEventDateTimeCalculator.calculateRepetitionDates(
                startDateTime, repetition, count);
        List<EventDateTimeSlot> eventDateTimeSlots = createEventDateTimeSlots(event, dateTimes);
        eventDateTimeSlotBulkRepository.saveAll(eventDateTimeSlots);
    }

    public void append(Event event, LocalDateTime startDateTime, Days days, int count) {
        Repetition repetition = event.getRepetition();
        List<LocalDateTime> dateTimes = recurringEventDateTimeCalculator.calculateRepetitionDates(
                startDateTime, repetition, days.toDayOfWeeks(), count);
        List<EventDateTimeSlot> eventDateTimeSlots = createEventDateTimeSlots(event, dateTimes);
        eventDateTimeSlotBulkRepository.saveAll(eventDateTimeSlots);
    }

    private List<EventDateTimeSlot> createEventDateTimeSlots(Event event, List<LocalDateTime> dateTimes) {
        return dateTimes.stream()
                .map(dateTime -> EventDateTimeSlot.of(event, dateTime))
                .toList();
    }
}
