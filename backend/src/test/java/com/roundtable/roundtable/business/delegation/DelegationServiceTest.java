package com.roundtable.roundtable.business.delegation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationRepository;
import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Day;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventDayOfWeek;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.Repetition;
import com.roundtable.roundtable.domain.event.RepetitionType;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventDayOfWeekRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class DelegationServiceTest extends IntegrationTestSupport {

    @Autowired
    private DelegationService sut;

    @Autowired
    private DelegationRepository delegationRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    @Autowired
    private EventDateTimeSlotRepository eventDateTimeSlotRepository;

    @Autowired
    private EventDayOfWeekRepository eventDayOfWeekRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("부탁을 승인한다.")
    @Test
    void approve() {
        //given
        House house = appendHouse("code");
        Member sender = appendMember(house, "email1");
        Member receiver = appendMember(house, "email2");
        Repetition repetition = Repetition.builder()
                .repetitionType(RepetitionType.WEEKLY)
                .repeatCycle(1)
                .build();
        Event event = appendEvent(repetition, LocalDateTime.now(), sender, house);
        EventDateTimeSlot eventDateTimeSlot = appendEventDateTimeSlot(event);
        EventParticipant eventParticipant = appendEventParticipant(event, sender);
        appendEventDayOfWeek(event, Day.MON, Day.FRI);
        Delegation delegation = appendDelegation(eventDateTimeSlot, sender, receiver, DelegationStatus.PENDING);

        //when
        sut.approve(delegation.getId(), new AuthMember(receiver.getId(), house.getId()),
                LocalDate.now());

        //then
        Event newEvent = eventDateTimeSlot.getEvent();
        assertThat(newEvent).isNotEqualTo(event);
        assertThat(newEvent).extracting("name", "parentEvent", "repetition")
                .containsExactly(event.getName(), event, repetition);

        List<EventParticipant> eventParticipants = eventParticipantRepository.findAllByEvent(newEvent);
        assertThat(eventParticipants).hasSize(1)
                .extracting("participant", "event")
                .containsExactly(tuple(receiver, newEvent));

        List<EventDayOfWeek> eventDayOfWeeks = eventDayOfWeekRepository.findByEvent(newEvent);
        assertThat(eventDayOfWeeks).hasSize(2)
                .extracting("dayOfWeek", "event")
                .containsExactly(tuple(Day.MON, newEvent), tuple(Day.FRI, newEvent));

        assertThat(delegation.getStatus()).isEqualTo(DelegationStatus.APPROVED);
    }

    private House appendHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    private Member appendMember(House house, String email) {
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

    private EventDateTimeSlot appendEventDateTimeSlot(Event event) {
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder()
                .event(event)
                .startTime(LocalDateTime.now())
                .isCompleted(false)
                .isSkipped(false)
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

    private Delegation appendDelegation(EventDateTimeSlot eventDateTimeSlot,
                                        Member sender,
                                        Member receiver,
                                        DelegationStatus status) {
        Delegation delegation = Delegation.builder()
                .status(status)
                .delegationDate(LocalDate.now().plusDays(10))
                .sender(sender)
                .receiver(receiver)
                .eventDateTimeSlot(eventDateTimeSlot)
                .message("부탁해요")
                .build();
        return delegationRepository.save(delegation);
    }

    private List<EventDayOfWeek> appendEventDayOfWeek(Event event, Day... days) {
        List<EventDayOfWeek> dayOfWeeks = Arrays.stream(days).map(day -> new EventDayOfWeek(event, day)).toList();
        return eventDayOfWeekRepository.saveAll(dayOfWeeks);
    }
}