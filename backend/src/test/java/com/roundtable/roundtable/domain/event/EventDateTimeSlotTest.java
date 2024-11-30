package com.roundtable.roundtable.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventDateTimeSlotTest {

    @DisplayName("이벤트를 완료한다.")
    @Test
    void complete() {
        //given
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder().isCompleted(false).build();

        //when
        eventDateTimeSlot.complete();

        //then
        assertThat(eventDateTimeSlot.isCompleted()).isTrue();
    }

    @DisplayName("이미 완료된 이벤트를 중복해서 완료하면 예외가 발생한다.")
    @Test
    void test() {
        //given
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder().isCompleted(true).build();

        //when //then
        assertThatThrownBy(eventDateTimeSlot::complete)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 완료된 이벤트입니다.");

    }
}