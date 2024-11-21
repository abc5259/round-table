package com.roundtable.roundtable.business.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.RepetitionType;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class EventDateTimeSlotAppenderTest extends IntegrationTestSupport {

    @Autowired
    private EventDateTimeSlotAppender eventDateTimeSlotAppender;
    @Autowired
    private EventDateTimeSlotRepository eventDateTimeSlotRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EventRepository eventRepository;

    @DisplayName("EventDateTimeSlot을 반복 날짜를 계산하여 데이터베이스에 저장한다.")
    @Test
    void append() {
        //given
        LocalDateTime startDateTime = LocalDateTime.of(2025, 1, 1, 1, 1);
        LocalDate repeatedUntilDate = LocalDate.of(2026, 1, 1);
        House house = createHouse("code");
        Member member = createMember(house, "abc");
        Repetition repetition = Repetition.builder()
                .repetitionType(RepetitionType.DAILY)
                .repeatCycle(3)
                .repeatedUntilDate(repeatedUntilDate)
                .build();
        Event event = appendEvent(repetition, startDateTime, member, house);

        //when
        eventDateTimeSlotAppender.append(event, startDateTime, 3);

        //then
        List<EventDateTimeSlot> eventDateTimeSlots = eventDateTimeSlotRepository.findAll();
        assertThat(eventDateTimeSlots).hasSize(3)
                .extracting("startTime", "isCompleted", "isSkipped", "event")
                .containsExactly(
                        Tuple.tuple(LocalDateTime.of(2025, 1, 1, 1, 1), false, false, event),
                        Tuple.tuple(LocalDateTime.of(2025, 1, 4, 1, 1), false, false, event),
                        Tuple.tuple(LocalDateTime.of(2025, 1, 7, 1, 1), false, false, event)
                );
    }

    public House createHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    public Member createMember(House house, String email) {
        Member member = Member.builder().house(house).email(email).password("password").build();
        return memberRepository.save(member);
    }

    private Event appendEvent(Repetition repetition, LocalDateTime startDateTime, Member member, House house) {
        Event event = Event.builder()
                .name("event")
                .category(Category.COOKING)
                .repetition(repetition)
                .startDateTime(startDateTime)
                .creator(member)
                .house(house)
                .build();
        eventRepository.save(event);
        return event;
    }
}