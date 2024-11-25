package com.roundtable.roundtable.domain.feedback;

import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feedback extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Emoji emoji;

    @NotNull
    @Column
    private String message;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private EventDateTimeSlot eventDateTimeSlot;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member sender;

    @Builder
    private Feedback(Long id, Emoji emoji, String message, EventDateTimeSlot eventDateTimeSlot, Member sender) {
        this.id = id;
        this.emoji = emoji;
        this.message = message;
        this.eventDateTimeSlot = eventDateTimeSlot;
        this.sender = sender;
    }


    public static Feedback create(Emoji emoji, String message, EventDateTimeSlot eventDateTimeSlot, Member sender) {
        //TODO sender와 Event가 같은 하우스인지 확인, 미완료 이벤트인지 확인
        return Feedback.builder()
                .emoji(emoji)
                .message(message)
                .eventDateTimeSlot(eventDateTimeSlot)
                .sender(sender)
                .build();
    }
}
