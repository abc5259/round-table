package com.roundtable.roundtable.business.notification;

import static org.assertj.core.api.Assertions.assertThat;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.domain.event.Category;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.notification.EventCompletionNotification;
import com.roundtable.roundtable.domain.notification.Notification;
import com.roundtable.roundtable.domain.notification.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class EventCompletionNotificationAppenderTest extends IntegrationTestSupport {

    @Autowired
    private EventCompletionNotificationAppender sut;

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

    @Autowired
    private NotificationRepository notificationRepository;

    @DisplayName("이벤트 완료 알림을 생성한다.")
    @Test
    void append() {
        //given
        House house = appendHouse();
        Member member1 = appendMember(house, "email1", "name1");
        Member member2 = appendMember(house, "email2", "name2");
        Member member3 = appendMember(house, "email3", "name3");
        Member member4 = appendMember(house, "email4", "name4");
        Event event = appendEvent(house, member1);
        EventDateTimeSlot eventDateTimeSlot = appendEventDateTimeSlot(event);
        appendEventParticipant(event, member2);
        appendEventParticipant(event, member3);

        //when
        sut.append(house.getId(), eventDateTimeSlot.getId(),
                List.of(member2.getId(), member3.getId()));

        //then
        List<Notification> notifications = notificationRepository.findAll();
        assertThat(notifications).hasSize(2);
        assertThat(notifications)
                .extracting("receiver")
                .contains(member1, member4);

        notifications.forEach(notification -> {
            assertThat((EventCompletionNotification) notification)
                    .extracting("eventId", "eventName", "memberNames")
                    .contains(event.getId(), event.getName(), member2.getName() + ", " + member3.getName());
        });
    }

    private House appendHouse() {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code("code").build()).build();
        houseRepository.save(house);
        return house;
    }

    private Member appendMember(House house, String email, String name) {
        Member member = Member.builder().name(name).email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Event appendEvent(House house, Member creator) {
        Event event = Event.builder()
                .creator(creator)
                .house(house)
                .name("event name")
                .category(Category.COOKING)
                .startDateTime(LocalDateTime.now())
                .build();
        return eventRepository.save(event);
    }

    private EventDateTimeSlot appendEventDateTimeSlot(Event event) {
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder()
                .startTime(LocalDateTime.now())
                .event(event)
                .isCompleted(true)
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