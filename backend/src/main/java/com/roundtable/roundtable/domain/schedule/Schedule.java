package com.roundtable.roundtable.domain.schedule;

import static com.roundtable.roundtable.domain.schedule.DivisionType.*;

import com.roundtable.roundtable.business.common.AuthMember;
import com.roundtable.roundtable.domain.common.BaseEntity;
import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

    public static final int START_SEQUENCE = 0;
    public static final int ONE_TIME_SCHEDULE_SEQUENCE_SIZE = 1;
    public static final int SEQUENCE_STEP = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private Integer sequence;

    @NotNull
    private Integer sequenceSize;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DivisionType divisionType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private House house;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleMember> scheduleMembers = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ExtraScheduleMember> extraScheduleMembers = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ScheduleDay> scheduleDays = new ArrayList<>();

    @Builder
    private Schedule(Long id,
                     String name,
                     LocalDate startDate,
                     LocalTime startTime,
                     Integer sequence,
                     Integer sequenceSize,
                     DivisionType divisionType,
                     ScheduleType scheduleType,
                     House house,
                     Category category) {

        this.name = name;
        this.startDate = startDate;
        this.startTime = startTime;
        this.sequence = sequence;
        this.sequenceSize = sequenceSize;
        this.divisionType = divisionType;
        this.scheduleType = scheduleType;
        this.house = house;
        this.category = category;
        this.id = id;
    }

    public static Schedule Id(Long scheduleId) {
        return Schedule.builder().id(scheduleId).build();
    }

    public static Schedule creatRepeatSchedule(
            String name,
            LocalDate startDate,
            LocalTime startTime,
            DivisionType divisionType,
            House house,
            int sequenceSize,
            Category category
    ) {
        if(divisionType == FIX) {
            sequenceSize = 1;
        }

        return Schedule.builder()
                .name(name)
                .startDate(startDate)
                .startTime(startTime)
                .divisionType(divisionType)
                .scheduleType(ScheduleType.REPEAT)
                .house(house)
                .sequence(START_SEQUENCE)
                .sequenceSize(sequenceSize)
                .category(category)
                .build();
    }

    public static Schedule createOneTimeSchedule(
            String name,
            LocalDate startDate,
            LocalTime startTime,
            House house
    ) {

        return Schedule.builder()
                .name(name)
                .startDate(startDate)
                .startTime(startTime)
                .divisionType(FIX)
                .scheduleType(ScheduleType.ONE_TIME)
                .house(house)
                .sequence(START_SEQUENCE)
                .sequenceSize(ONE_TIME_SCHEDULE_SEQUENCE_SIZE)
                .category(Category.ONE_TIME)
                .build();
    }

    public boolean isEqualSequence(Integer sequence) {
        return this.sequence.equals(sequence);
    }

    public boolean isSameHouse(Member member) {
        return member.isSameHouse(house);
    }

    public boolean isSameHouse(long houseId) {
        return house.getId() == houseId;
    }

    public void complete() {
        increaseSequence();
    }

    private void increaseSequence() {
        if(divisionType == FIX) {
            return;
        }

        sequence = (sequence + SEQUENCE_STEP) % sequenceSize;
    }

    public void validateSameHouse(Member sender) {
        if(!sender.isSameHouse(house)) {
            throw new IllegalArgumentException("같은 하우스의 사용자만 피드백을 보낼 수 있습니다.");
        }
    }

    public Day getStartDay() {
        return Day.forDayOfWeek(startDate.getDayOfWeek());
    }

    public boolean isSameDivisionType(DivisionType divisionType) {
        return this.divisionType == divisionType;
    }
}
