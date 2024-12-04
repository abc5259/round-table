package com.roundtable.roundtable.business.event;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.dto.EventDateTimeSlotDetailDto;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class EventDateTimeSlotServiceTest extends IntegrationTestSupport {

    @Autowired
    private EventDateTimeSlotService sut;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    @Autowired
    private EventDateTimeSlotRepository eventDateTimeSlotRepository;

    @DisplayName("날짜에 따른 이벤트 세부 정보 조회")
    @Test
    void findEventDateTimeSlotDetails() {
        //given
        House house = appendHouse("code");
        Member member1 = appendMember(house, "email1", "name1");
        Member member2 = appendMember(house, "email2", "name2");
        Event event = appendEvent(null, LocalDateTime.now(), member1, house, "event1");
        EventDateTimeSlot eventDateTimeSlot = appendEventDateTimeSlot(event, LocalDateTime.of(2024, 12, 2, 0, 0),
                false);
        appendEventParticipant(event, member1);

        Event event2 = appendEvent(null, LocalDateTime.now(), member1, house, "event2");
        EventDateTimeSlot eventDateTimeSlot2 = appendEventDateTimeSlot(event2, LocalDateTime.of(2024, 12, 2, 10, 0),
                true);
        appendEventParticipant(event2, member1);
        appendEventParticipant(event2, member2);

        //when
        List<EventDateTimeSlotDetailDto> result = sut.findEventDateTimeSlotDetails(
                new AuthMember(member1.getId(), house.getId()), LocalDate.of(2024, 12, 2));

        //then
        Assertions.assertThat(result).hasSize(2)
                .containsExactly(
                        new EventDateTimeSlotDetailDto(
                                event.getId(),
                                eventDateTimeSlot.getId(),
                                event.getName(),
                                event.getCategory(),
                                eventDateTimeSlot.isCompleted(),
                                eventDateTimeSlot.getStartTime(),
                                "name1"
                        ),
                        new EventDateTimeSlotDetailDto(
                                event2.getId(),
                                eventDateTimeSlot2.getId(),
                                event2.getName(),
                                event2.getCategory(),
                                eventDateTimeSlot2.isCompleted(),
                                eventDateTimeSlot2.getStartTime(),
                                "name1,name2"
                        )
                );
    }

    private House appendHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    private Member appendMember(House house, String email, String name) {
        Member member = Member.builder().name(name).house(house).email(email).password("password").build();
        return memberRepository.save(member);
    }

    private Event appendEvent(Repetition repetition,
                              LocalDateTime startDateTime,
                              Member member,
                              House house,
                              String eventName) {
        Event event = Event.builder()
                .name(eventName)
                .category(Category.COOKING)
                .repetition(repetition)
                .startDateTime(startDateTime)
                .creator(member)
                .house(house)
                .build();
        eventRepository.save(event);
        return event;
    }

    private EventDateTimeSlot appendEventDateTimeSlot(Event event, LocalDateTime dateTime, boolean isCompleted) {
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder()
                .event(event)
                .startTime(dateTime)
                .isSkipped(false)
                .isCompleted(isCompleted)
                .build();
        return eventDateTimeSlotRepository.save(eventDateTimeSlot);
    }

    private EventParticipant appendEventParticipant(Event event, Member member) {
        EventParticipant eventParticipant = EventParticipant.builder()
                .event(event)
                .participant(member)
                .build();
        return eventParticipantRepository.save(eventParticipant);
    }
}