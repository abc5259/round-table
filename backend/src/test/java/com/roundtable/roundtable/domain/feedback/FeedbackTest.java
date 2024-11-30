package com.roundtable.roundtable.domain.feedback;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.EventParticipants;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedbackTest {

    @DisplayName("자신이 수행한 이벤트에 피드백을 생성하면 예외가 발생한다.")
    @Test
    void createFeedbackForOwnEventThrowsException() {
        //given
        House house = createHouse(1L);
        Member feedbackSender = createMember(1L, "email1", house);
        Event event = createEvent(feedbackSender, house);
        EventParticipant eventParticipant = createEventParticipant(event, feedbackSender);
        EventDateTimeSlot eventDateTimeSlot = createEventDateTimeSlot(event, true);

        //when then
        assertThatThrownBy(
                () -> Feedback.create(Emoji.FIRE, "깔끔해", eventDateTimeSlot, feedbackSender, new EventParticipants(
                        List.of(eventParticipant))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이벤트에 참여하지 않는 사람만 피드백이 가능합니다.");
    }

    @DisplayName("자신과 다른 하우스의 스케줄에 피드백을 생성하려하면 예외가 발생한다.")
    @Test
    void createFeedbackWhenOtherHouseScheduleThrowsException() {
        //given
        Emoji emoji = Emoji.FIRE;
        String message = "깔끔해";
        House house1 = createHouse(1L);
        House house2 = createHouse(2L);
        Member feedbackSender = createMember(1L, "email1", house1);
        Member eventMember = createMember(2L, "email2", house2);
        Event event = createEvent(eventMember, house2);
        EventDateTimeSlot eventDateTimeSlot = createEventDateTimeSlot(event, true);
        EventParticipant eventParticipant = createEventParticipant(event, eventMember);

        //when then
        assertThatThrownBy(
                () -> Feedback.create(emoji, message, eventDateTimeSlot, feedbackSender, new EventParticipants(
                        List.of(eventParticipant))))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 하우스에 있는 이벤트에만 피드백을 할 수 있습니다.");
    }

    @DisplayName("미완료 이벤트에 대해서 피드백을 생성하면 예외가 발생한다.")
    @Test
    void createFeedbackWhenIncompleteEvent() {
        //given
        Emoji emoji = Emoji.FIRE;
        String message = "깔끔해";
        House house = createHouse(1L);
        Member eventMember = createMember(1L, "email1", house);
        Member feedbackSender = createMember(2L, "email2", house);
        Event event = createEvent(eventMember, house);
        EventDateTimeSlot eventDateTimeSlot = createEventDateTimeSlot(event, false);
        EventParticipant eventParticipant = createEventParticipant(event, eventMember);

        //when then
        assertThatThrownBy(
                () -> Feedback.create(emoji, message, eventDateTimeSlot, feedbackSender, new EventParticipants(
                        List.of(eventParticipant))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("완료되지 않은 스케줄에는 피드백을 보낼 수 없습니다.");

    }

    private Member createMember(long id, String email1, House house1) {
        return Member.builder().id(id).email(email1).house(house1).password("password").build();
    }

    private House createHouse(long id) {
        return House.builder()
                .id(id)
                .name("부산 하우스")
                .inviteCode(InviteCode.builder().code("code").build())
                .build();
    }

    private Event createEvent(Member member, House house) {
        return Event.builder()
                .creator(member)
                .house(house)
                .category(Category.COOKING).name("event")
                .startDateTime(LocalDateTime.now())
                .build();
    }

    private EventParticipant createEventParticipant(Event event, Member member) {
        return EventParticipant.builder()
                .event(event)
                .participant(member)
                .build();
    }

    private EventDateTimeSlot createEventDateTimeSlot(Event event, boolean isCompleted) {
        return EventDateTimeSlot.builder()
                .startTime(LocalDateTime.now())
                .event(event)
                .isCompleted(isCompleted)
                .build();
    }
}