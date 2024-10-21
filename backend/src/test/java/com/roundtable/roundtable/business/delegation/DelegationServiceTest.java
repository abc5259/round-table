package com.roundtable.roundtable.business.delegation;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.roundtable.roundtable.IntegrationTestSupport;
import com.roundtable.roundtable.business.delegation.dto.CreateDelegationDto;
import com.roundtable.roundtable.business.delegation.event.CreateDelegationEvent;
import com.roundtable.roundtable.business.delegation.event.UpdateDelegationEvent;
import com.roundtable.roundtable.domain.delegation.Delegation;
import com.roundtable.roundtable.domain.delegation.DelegationRepository;
import com.roundtable.roundtable.domain.delegation.DelegationStatus;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.house.HouseRepository;
import com.roundtable.roundtable.domain.house.InviteCode;
import com.roundtable.roundtable.domain.member.Member;
import com.roundtable.roundtable.domain.member.MemberRepository;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.ExtraScheduleMember;
import com.roundtable.roundtable.domain.schedule.repository.ExtraScheduleMemberRepository;
import com.roundtable.roundtable.domain.schedule.Schedule;
import com.roundtable.roundtable.domain.schedule.ScheduleCompletion;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleCompletionRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleDay;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleDayRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleMember;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleMemberRepository;
import com.roundtable.roundtable.domain.schedule.repository.ScheduleRepository;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import com.roundtable.roundtable.global.exception.DelegationException;
import com.roundtable.roundtable.global.exception.errorcode.DelegationErrorCode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RecordApplicationEvents
class DelegationServiceTest extends IntegrationTestSupport {

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ScheduleMemberRepository scheduleMemberRepository;

    @Autowired
    private ScheduleDayRepository scheduleDayRepository;

    @Autowired
    private DelegationRepository delegationRepository;

    @Autowired
    private ScheduleCompletionRepository scheduleCompletionRepository;

    @Autowired
    private ExtraScheduleMemberRepository extraScheduleMemberRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private DelegationService sut;


    @DisplayName("Delegation을 생성한다.")
    @Test
    void createDelegation() {
        //given
        LocalDate now = LocalDate.of(2024, 9, 30);
        House house = appendHouse("code1");
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, DivisionType.ROTATION, ScheduleType.REPEAT);
        appendScheduleMember(schedule, member1, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(now.getDayOfWeek()));
        CreateDelegationDto createDelegationDto = new CreateDelegationDto(schedule.getId(), "부탁해", member1.getId(), member2.getId(), now);

        //when
        Long resultId = sut.createDelegation(house.getId(), createDelegationDto);

        //then
        Delegation delegation = delegationRepository.findById(resultId).orElseThrow();
        assertThat(delegation)
                .extracting("message", "status", "delegationDate")
                .contains("부탁해", DelegationStatus.PENDING, now);
        assertThat(delegation.getSchedule().getId()).isEqualTo(schedule.getId());
        assertThat(delegation.getSender().getId()).isEqualTo(member1.getId());
        assertThat(delegation.getReceiver().getId()).isEqualTo(member2.getId());
        assertThat(applicationEvents.stream(CreateDelegationEvent.class))
                .hasSize(1)
                .anySatisfy(event -> {
                    assertAll(
                            () -> assertThat(event.houseId()).isEqualTo(house.getId()),
                            () -> assertThat(event.sender()).isEqualTo(member1),
                            () -> assertThat(event.receiver()).isEqualTo(member2),
                            () -> assertThat(event.scheduleId()).isEqualTo(schedule.getId()),
                            () -> assertThat(event.delegation()).isEqualTo(delegation)
                    );
                });
    }

    @DisplayName("이미 부탁을 했다면 예외가 발생한다.")
    @Test
    void appendDelegationWhenAlreadyExistThrowsException() {
        //given
        LocalDate now = LocalDate.of(2024, 9, 30);
        House house = appendHouse("code1");
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, DivisionType.ROTATION, ScheduleType.REPEAT);
        appendScheduleMember(schedule, member1, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(now.getDayOfWeek()));
        CreateDelegationDto createDelegationDto = new CreateDelegationDto(schedule.getId(), "부탁해", member1.getId(), member2.getId(), now);
        appendDelegation(now, member1, member2, schedule);

        //when
        assertThatThrownBy(() -> sut.createDelegation(house.getId(), createDelegationDto))
                .isInstanceOf(DelegationException.class)
                .hasMessage(DelegationErrorCode.ALREADY_EXIST_DELEGATION.getMessage());
    }

    @DisplayName("오늘 수행하지 않는 스케줄에 부탁을 하면 예외가 발생한다.")
    @Test
    void appendDelegationWhenNotTodayScheduleThrowsException() {
        //given
        LocalDate now = LocalDate.of(2024, 9, 30);
        House house = appendHouse("code1");
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, DivisionType.ROTATION, ScheduleType.REPEAT);
        appendScheduleMember(schedule, member1, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(now.getDayOfWeek().plus(1)));
        CreateDelegationDto createDelegationDto = new CreateDelegationDto(schedule.getId(), "부탁해", member1.getId(), member2.getId(), now);

        //when
        assertThatThrownBy(() -> sut.createDelegation(house.getId(), createDelegationDto))
                .isInstanceOf(DelegationException.class)
                .hasMessage(DelegationErrorCode.DELEGATION_FORBIDDEN_NOT_TODAY_SCHEDULE.getMessage());
    }

    @DisplayName("이미 완료한 스케줄에 부탁을 하면 예외가 발생한다.")
    @Test
    void appendDelegationWhenAlreadyCompletionScheduleThrowsException() {
        //given
        LocalDate now = LocalDate.of(2024, 9, 30);
        House house = appendHouse("code1");
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, DivisionType.ROTATION, ScheduleType.REPEAT);
        appendScheduleMember(schedule, member1, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(now.getDayOfWeek()));
        appendScheduleCompletion(schedule, now);

        CreateDelegationDto createDelegationDto = new CreateDelegationDto(schedule.getId(), "부탁해", member1.getId(), member2.getId(), now);

        //when
        assertThatThrownBy(() -> sut.createDelegation(house.getId(), createDelegationDto))
                .isInstanceOf(DelegationException.class)
                .hasMessage(DelegationErrorCode.DELEGATION_FORBIDDEN_ALREADY_COMPLETION_SCHEDULE.getMessage());
    }

    @DisplayName("Delegation의 상태를 업데이트한다.")
    @Test
    void updateDelegationStatus() {
        //given
        LocalDate now = LocalDate.of(2024, 9, 30);
        House house = appendHouse("code1");
        Member member1 = appendMember(house, "email1");
        Member member2 = appendMember(house, "email2");
        Schedule schedule = appendSchedule(house, DivisionType.ROTATION, ScheduleType.REPEAT);
        appendScheduleMember(schedule, member1, 0);
        appendScheduleDay(schedule, Day.forDayOfWeek(now.getDayOfWeek()));
        Delegation delegation = appendDelegation(now, member1, member2, schedule);

        //when
        sut.approveDelegation(house.getId(), member2.getId(), delegation.getId(), now);

        //then
        Delegation result = delegationRepository.findById(delegation.getId()).orElseThrow();
        assertThat(result.getStatus()).isEqualTo(DelegationStatus.APPROVED);

        List<ExtraScheduleMember> extraScheduleMembers = extraScheduleMemberRepository.findAll();
        assertThat(extraScheduleMembers).hasSize(1)
                .extracting("schedule", "member.id", "assignedDate")
                .contains(
                        Tuple.tuple(schedule, member2.getId(), now)
                );

        assertThat(applicationEvents.stream(UpdateDelegationEvent.class))
                .hasSize(1)
                .anySatisfy(event -> {
                    assertAll(
                            () -> assertThat(event.delegation()).isEqualTo(delegation)
                    );
                });
    }


    private House appendHouse(String code) {
        House house = House.builder().name("house1").inviteCode(InviteCode.builder().code(code).build()).build();
        houseRepository.save(house);
        return house;
    }

    private Member appendMember(House house, String email) {
        Member member = Member.builder().email(email).password("password").house(house).build();
        return memberRepository.save(member);
    }

    private Schedule appendSchedule(House house, DivisionType divisionType, ScheduleType scheduleType) {
        Schedule schedule = Schedule.builder()
                .name("schedule")
                .category(Category.COOKING)
                .startDate(LocalDate.now())
                .startTime(LocalTime.MAX)
                .sequence(0)
                .sequenceSize(2)
                .house(house)
                .divisionType(divisionType)
                .scheduleType(scheduleType)
                .build();
        return scheduleRepository.save(schedule);
    }

    private ScheduleMember appendScheduleMember(Schedule schedule, Member member, int sequence) {
        ScheduleMember scheduleMember = ScheduleMember.builder()
                .schedule(schedule)
                .member(member)
                .sequence(sequence)
                .build();
        return scheduleMemberRepository.save(scheduleMember);
    }

    private ScheduleDay appendScheduleDay(Schedule schedule, Day day) {
        ScheduleDay scheduleDay = ScheduleDay.builder()
                .schedule(schedule)
                .dayOfWeek(day)
                .build();
        return scheduleDayRepository.save(scheduleDay);
    }

    private Delegation appendDelegation(LocalDate now, Member member1, Member member2, Schedule schedule) {
        Delegation delegation = Delegation.builder().delegationDate(now).sender(member1).receiver(member2)
                .status(DelegationStatus.PENDING).message("message").schedule(schedule).build();
        return delegationRepository.save(delegation);
    }

    private void appendScheduleCompletion(Schedule schedule, LocalDate date) {
        ScheduleCompletion scheduleCompletion = ScheduleCompletion.builder()
                .schedule(schedule)
                .sequence(0)
                .completionDate(date)
                .build();
        scheduleCompletionRepository.save(scheduleCompletion);
    }
}