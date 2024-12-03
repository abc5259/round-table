package com.roundtable.roundtable.domain.delegation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DelegationTest {

    @DisplayName("부탁을 생성한다.")
    @Test
    void create() {
        //given
        House house = createHouse(1L);
        Member sender = createMember(1L, house);
        Member receiver = createMember(2L, house);
        Event event = Event.builder().id(1L).name("event").build();
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder().id(1L).event(event).isCompleted(false)
                .build();
        EventParticipant eventParticipant = EventParticipant.builder().event(event).participant(sender).build();

        //when
        Delegation delegation = Delegation.create(
                List.of(eventParticipant),
                "부탁해",
                eventDateTimeSlot,
                sender,
                receiver,
                LocalDate.of(2022, 10, 1)
        );

        //then
        assertThat(delegation)
                .extracting("message", "sender", "receiver", "eventDateTimeSlot", "status", "delegationDate")
                .containsExactly(
                        "부탁해", sender, receiver, eventDateTimeSlot, DelegationStatus.PENDING, LocalDate.of(2022, 10, 1)
                );
    }

    @DisplayName("다른 하우스에 있는 사용자에게 부탁을 한 경우 예외가 발생한다.")
    @Test
    void createWhenNoParticipantSenderIsThrowException() {
        //given
        House house1 = createHouse(1L);
        House house2 = createHouse(2L);
        Member sender = createMember(1L, house1);
        Member receiver = createMember(2L, house2);
        Event event = Event.builder().id(1L).name("event").build();
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder().id(1L).event(event).isCompleted(false)
                .build();
        EventParticipant eventParticipant = EventParticipant.builder().event(event).participant(sender).build();

        //when //then
        assertThatThrownBy(() -> Delegation.create(
                List.of(eventParticipant),
                "부탁해",
                eventDateTimeSlot,
                sender,
                receiver,
                LocalDate.of(2022, 10, 1)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 하우스의 사용자에게만 부탁이 가능합니다.");
    }

    @DisplayName("이미 완료된 이벤트에 대해 부탁을 만들면 예외가 발생한다.")
    @Test
    void createAlreadyCompletedEventThrowException() {
        //given
        House house1 = createHouse(1L);
        Member sender = createMember(1L, house1);
        Member receiver = createMember(2L, house1);
        Event event = Event.builder().id(1L).name("event").build();
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder().id(1L).event(event).isCompleted(true)
                .build();
        EventParticipant eventParticipant = EventParticipant.builder().event(event).participant(sender).build();

        //when //then
        assertThatThrownBy(() -> Delegation.create(
                List.of(eventParticipant),
                "부탁해",
                eventDateTimeSlot,
                sender,
                receiver,
                LocalDate.of(2022, 10, 1)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 이벤트는 부탁할 수 없습니다.");
    }

    @DisplayName("부탁하는 하는 sender가 이벤트 참여자가 아닌 경우 예외가 발생한다.")
    @Test
    void createWhenSenderIsNotEventParticipantThrowException() {
        //given
        House house1 = createHouse(1L);
        Member sender = createMember(1L, house1);
        Member receiver = createMember(2L, house1);
        Member member = createMember(3L, house1);
        Event event = Event.builder().id(1L).name("event").build();
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder().id(1L).event(event).isCompleted(true)
                .build();
        EventParticipant eventParticipant = EventParticipant.builder().event(event).participant(member).build();

        //when //then
        assertThatThrownBy(() -> Delegation.create(
                List.of(eventParticipant),
                "부탁해",
                eventDateTimeSlot,
                sender,
                receiver,
                LocalDate.of(2022, 10, 1)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("부탁은 이벤트의 담당자만 가능합니다.");
    }

    @DisplayName("부탁을 받는 receiver가 이미 이벤트 참여자라면 예외가 발생한다.")
    @Test
    void createWhenReceiverIsEventParticipantThrowException() {
        //given
        House house1 = createHouse(1L);
        Member sender = createMember(1L, house1);
        Member receiver = createMember(2L, house1);
        Event event = Event.builder().id(1L).name("event").build();
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder().id(1L).event(event).isCompleted(true)
                .build();
        EventParticipant eventParticipant1 = EventParticipant.builder().event(event).participant(sender).build();
        EventParticipant eventParticipant2 = EventParticipant.builder().event(event).participant(receiver).build();

        //when //then
        assertThatThrownBy(() -> Delegation.create(
                List.of(eventParticipant1, eventParticipant2),
                "부탁해",
                eventDateTimeSlot,
                sender,
                receiver,
                LocalDate.of(2022, 10, 1)
        ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이벤트 담당자에게 부탁을 할 수 없습니다.");
    }

    private Member createMember(long id, House house) {
        return Member.builder().id(id).house(house).build();
    }

    private House createHouse(long id) {
        return House.builder().id(id).name("house").build();
    }

}