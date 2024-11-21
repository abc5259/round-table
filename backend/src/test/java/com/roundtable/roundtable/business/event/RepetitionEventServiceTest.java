package com.roundtable.roundtable.business.event;

import static org.assertj.core.api.Assertions.assertThat;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto;
import com.roundtable.roundtable.business.event.dto.CreateRepetitionEventDto.RepetitionDto;
import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.RepetitionType;
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
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class RepetitionEventServiceTest extends IntegrationTestSupport {

    @Autowired
    private RepetitionEventService repetitionEventService;
    @Autowired
    private EventDateTimeSlotRepository eventDateTimeSlotRepository;
    @Autowired
    private HouseRepository houseRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    @DisplayName("반복 일정을 생성한다.")
    @Test
    void createEvent() {
        //given
        House house = createHouse("code");
        Member member1 = createMember(house, "email1");
        Member member2 = createMember(house, "email2");
        LocalDateTime startDateTime = LocalDateTime.of(2023, 6, 28, 3, 0);
        CreateRepetitionEventDto createRepetitionEventDto = new CreateRepetitionEventDto(
                "testEvent",
                Category.COOKING,
                startDateTime,
                new RepetitionDto(
                        RepetitionType.DAILY,
                        3,
                        LocalDate.of(2024, 6, 28),
                        new ArrayList<>()
                ),
                List.of(member1.getId(), member2.getId())
        );

        //when
        long eventId = repetitionEventService.createEvent(
                createRepetitionEventDto,
                new AuthMember(member1.getId(), house.getId()),
                startDateTime.toLocalDate());

        //then
        Event event = eventRepository.findById(eventId).orElseThrow();
        assertThat(event)
                .extracting("name", "category", "startDateTime", "repetition.repetitionType", "repetition.repeatCycle",
                        "house.id", "creator.id", "parentEvent")
                .containsExactly(
                        createRepetitionEventDto.eventName(),
                        createRepetitionEventDto.category(),
                        createRepetitionEventDto.startDateTime(),
                        RepetitionType.DAILY,
                        3,
                        house.getId(),
                        member1.getId(),
                        null
                );

        List<EventDateTimeSlot> eventDateTimeSlots = eventDateTimeSlotRepository.findAll();
        assertThat(eventDateTimeSlots).hasSize(30);

        List<EventParticipant> eventParticipants = eventParticipantRepository.findAll();
        assertThat(eventParticipants).hasSize(2)
                .extracting("event", "participant")
                .contains(
                        Tuple.tuple(event, member1),
                        Tuple.tuple(event, member2)
                );
    }

    private House createHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    private Member createMember(House house, String email) {
        Member member = Member.builder().house(house).email(email).password("password").build();
        return memberRepository.save(member);
    }
}