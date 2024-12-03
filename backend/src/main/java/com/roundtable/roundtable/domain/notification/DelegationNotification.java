package com.roundtable.roundtable.domain.notification;

import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.notification.NotificationType.Values;
import com.roundtable.roundtable.domain.sse.event.DelegationSseEvent;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue(Values.DELEGATION)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DelegationNotification extends Notification {
    private Long delegationId;
    private DelegationStatus status;
    private String eventName;

    @Builder
    private DelegationNotification(
            Member sender,
            Member receiver,
            House house,
            Long delegationId,
            DelegationStatus status,
            String eventName
    ) {
        super(sender, receiver, house);
        this.delegationId = delegationId;
        this.status = status;
        this.eventName = eventName;
    }

    public static DelegationNotification create(
            House house,
            Member sender,
            Member receiver,
            Delegation delegation,
            String eventName
    ) {
        return DelegationNotification.builder()
                .sender(sender)
                .receiver(receiver)
                .house(house)
                .delegationId(delegation.getId())
                .status(delegation.getStatus())
                .eventName(eventName)
                .build();
    }

//    public static DelegationNotification create(
//            House house,
//            Delegation delegation
//    ) {
//        return DelegationNotification.builder()
//                .sender(delegation.getReceiver())
//                .receiver(delegation.getSender())
//                .house(house)
//                .delegationId(delegation.getId())
//                .status(delegation.getStatus())
//                .eventName(delegation.getScheduleName())
//                .build();
//    }

    public DelegationSseEvent toSseEvent() {
        return DelegationSseEvent.of(getSender().getName(), delegationId, status, eventName);
    }
}
