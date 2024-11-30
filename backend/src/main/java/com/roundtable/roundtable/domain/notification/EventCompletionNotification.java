package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import com.roundtable.roundtable.domain.sse.event.ScheduleCompletionSseEvent;
import com.roundtable.roundtable.domain.sse.event.ScheduleCompletionSseEvent.ScheduleCompletionSseData;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Values.EVENT_COMPLETION)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EventCompletionNotification extends Notification {

    private Long eventId;
    private String eventName;
    /**
     * member가 여러명일경우 ,로 구분
     */
    private String memberNames;

    @Builder
    private EventCompletionNotification(Member receiver, House house, Long eventId, String eventName,
                                        String memberNames) {
        super(receiver, house);
        this.eventId = eventId;
        this.eventName = eventName;
        this.memberNames = memberNames;
    }

    public static EventCompletionNotification create(Member receiver,
                                                     House house,
                                                     Long eventId,
                                                     String eventName,
                                                     List<Member> members) {
        String names = members.stream()
                .map(Member::getName)
                .collect(Collectors.joining(", "));

        return EventCompletionNotification.builder()
                .receiver(receiver)
                .house(house)
                .eventId(eventId)
                .eventName(eventName)
                .memberNames(names)
                .build();
    }

    public ScheduleCompletionSseEvent toSseEvent() {
        ScheduleCompletionSseData eventData = new ScheduleCompletionSseData(eventId, eventName, memberNames);
        return new ScheduleCompletionSseEvent(eventData);
    }
}
