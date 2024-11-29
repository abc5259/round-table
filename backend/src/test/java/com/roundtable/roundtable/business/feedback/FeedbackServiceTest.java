package com.roundtable.roundtable.business.feedback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.feedback.dto.CreateFeedbackServiceDto;
import com.roundtable.roundtable.business.feedback.event.CreateFeedbackEvent;
import com.roundtable.roundtable.domain.event.Event;
import com.roundtable.roundtable.domain.event.EventDateTimeSlot;
import com.roundtable.roundtable.domain.event.EventParticipant;
import com.roundtable.roundtable.domain.event.repository.EventDateTimeSlotRepository;
import com.roundtable.roundtable.domain.event.repository.EventParticipantRepository;
import com.roundtable.roundtable.domain.event.repository.EventRepository;
import com.roundtable.roundtable.domain.feedback.Emoji;
import com.roundtable.roundtable.domain.feedback.Feedback;
import com.roundtable.roundtable.domain.feedback.FeedbackRepository;
import com.roundtable.roundtable.domain.feedback.FeedbackSelection;
import com.roundtable.roundtable.domain.feedback.FeedbackSelectionRepository;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedback;
import com.roundtable.roundtable.domain.feedback.PredefinedFeedbackRepository;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RecordApplicationEvents
class FeedbackServiceTest extends IntegrationTestSupport {

    @Autowired
    private FeedbackService sut;

    @Autowired
    private ApplicationEvents events;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackSelectionRepository feedbackSelectionRepository;

    @Autowired
    private PredefinedFeedbackRepository predefinedFeedbackRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EventParticipantRepository eventParticipantRepository;

    @Autowired
    private EventDateTimeSlotRepository eventDateTimeSlotRepository;

    @BeforeEach
    public void setUp() {
        createPredefinedFeedbacks();
    }

    @DisplayName("Feedback을 추가할 수 있다.")
    @Test
    void append() {
        /**
         * 피드백은 어디에 줄 수 있을까?
         * 일단 완료된 스케줄에 준다.
         * 완료된 스케줄에서 자기가 완료한 스케줄에는 줄 수 없다.
         * 다른 하우스의 스케줄에도 피드백을 주면 안됨
         */
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Member member = createMember("email2", house);
        Event event = createEvent(sender, house);
        createEventParticipant(event, member);
        EventDateTimeSlot eventDateTimeSlot = createEventDateTimeSlot(event, true);

        CreateFeedbackServiceDto createFeedbackServiceDto = new CreateFeedbackServiceDto(Emoji.FIRE, "좋아요",
                sender.getId(), event.getId(), eventDateTimeSlot.getId(), List.of(1, 2));

        //when
        Long feedbackId = sut.createFeedback(createFeedbackServiceDto, house.getId());

        //then
        Feedback feedback = feedbackRepository.findById(feedbackId).orElseThrow();
        List<FeedbackSelection> feedbackSelections = feedbackSelectionRepository.findAll();

        assertThat(feedback)
                .extracting("emoji", "message", "sender", "eventDateTimeSlot")
                .contains(
                        Emoji.FIRE,
                        "좋아요",
                        sender,
                        eventDateTimeSlot
                );
        assertThat(feedbackSelections).hasSize(2)
                .extracting("feedback")
                .contains(feedback, feedback);
        assertThat(events.stream(CreateFeedbackEvent.class)).hasSize(1)
                .anySatisfy(e -> {
                    assertAll(
                            () -> assertThat(e.houseId()).isEqualTo(house.getId()),
                            () -> assertThat(e.feedbackId()).isEqualTo(feedback.getId()),
                            () -> assertThat(e.senderId()).isEqualTo(sender.getId()),
                            () -> assertThat(e.eventId()).isEqualTo(event.getId())
                    );
                });
    }

    @DisplayName("완료된 스케줄이 아니라면 예외가 발생한다.")
    @Test
    void append_feedback_when_not_completion_schedule_throw_exception() {
        //given
        House house = createHouse("code");
        Member sender = createMember("email1", house);
        Member member = createMember("email2", house);
        Event event = createEvent(sender, house);
        createEventParticipant(event, member);
        EventDateTimeSlot eventDateTimeSlot = createEventDateTimeSlot(event, false);

        CreateFeedbackServiceDto createFeedbackServiceDto = new CreateFeedbackServiceDto(Emoji.FIRE, "좋아요",
                sender.getId(), event.getId(), eventDateTimeSlot.getId(), List.of(1, 2));

        //when //then
        assertThatThrownBy(() -> sut.createFeedback(createFeedbackServiceDto, house.getId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("완료되지 않은 스케줄에는 피드백을 보낼 수 없습니다.");
    }

    public House createHouse(String code) {
        House house = House.builder().name("name").inviteCode(InviteCode.builder().code(code).build()).build();
        return houseRepository.save(house);
    }

    public Member createMember(String email, House house) {
        Member member = Member.builder().email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Event createEvent(Member member, House house) {
        Event event = Event.builder()
                .creator(member)
                .house(house)
                .startDateTime(LocalDateTime.now())
                .name("test event")
                .category(com.roundtable.roundtable.domain.event.Category.COOKING)
                .build();
        return eventRepository.save(event);
    }

    private EventDateTimeSlot createEventDateTimeSlot(Event event, boolean isCompleted) {
        EventDateTimeSlot eventDateTimeSlot = EventDateTimeSlot.builder()
                .event(event)
                .startTime(LocalDateTime.now())
                .isCompleted(isCompleted)
                .isSkipped(false)
                .build();
        return eventDateTimeSlotRepository.save(eventDateTimeSlot);
    }

    private EventParticipant createEventParticipant(Event event, Member member) {
        EventParticipant eventParticipant = EventParticipant.builder().event(event).participant(member).build();
        return eventParticipantRepository.save(eventParticipant);
    }

    public void createPredefinedFeedbacks() {
        String[] feedbackTexts = {"good", "god", "so good"};

        for (int i = 0; i < 3; i++) {

            predefinedFeedbackRepository.save(
                    PredefinedFeedback.builder().id(i + 1).feedbackText(feedbackTexts[i]).build());
        }
    }
}