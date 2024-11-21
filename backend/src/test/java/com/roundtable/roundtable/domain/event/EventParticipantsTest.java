package com.roundtable.roundtable.domain.event;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventParticipantsTest {

    @DisplayName("하우스가 다른 맴버로 이벤트 참여자를 만들경우 예외가 발생한다.")
    @Test
    void createEventParticipants() {
        //given
        House house1 = House.builder().id(1L).build();
        House house2 = House.builder().id(2L).build();
        Event event = new Event();
        Member member1 = Member.builder().id(1L).name("name1").house(house1).build();
        Member member2 = Member.builder().id(2L).name("name2").house(house2).build();

        //when //then
        assertThatThrownBy(() -> new EventParticipants(event, List.of(member1, member2), house1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 하우스에 속한 사용자만 참여할 수 있습니다.");
    }
}