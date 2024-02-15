package com.roundtable.roundtable.business.schedule;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.roundtable.roundtable.entity.chore.Chore;
import com.roundtable.roundtable.entity.house.House;
import com.roundtable.roundtable.entity.member.Member;
import com.roundtable.roundtable.entity.schedule.DivisionType;
import com.roundtable.roundtable.entity.schedule.FrequencyType;
import com.roundtable.roundtable.implement.schedule.CreateSchedule;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ScheduleServiceTest {

    @Autowired
    private ScheduleService scheduleService;


    @Autowired
    private EntityManager em;

    @DisplayName("시작날짜가 오늘과 같다면 스케줄과 집안일 둘다 생성한다.")
    @Test
    void createScheduleWhenStartDateWithToday() {
        //given
        LocalDate startDate = LocalDate.now();
        LocalDate now = LocalDate.now();

        House house = createHouse();
        Member member = createMemberInHouse(house);

        CreateSchedule createSchedule = new CreateSchedule(
                "schedule1",
                FrequencyType.DAILY,
                2,
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                List.of(member.getId())
        );

        //when
        Long scheduleId = scheduleService.createSchedule(createSchedule, member, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();
        assertThat(chore).hasSize(1);
    }

    @DisplayName("시작날짜가 오늘이 아니라면 스케줄만 생성한다.")
    @Test
    void createScheduleWhenStartDateWithNotToday() {
        //given
        LocalDate startDate = LocalDate.of(2024,2,15);
        LocalDate now = LocalDate.of(2024,2,14);

        House house = createHouse();
        Member member = createMemberInHouse(house);

        CreateSchedule createSchedule = new CreateSchedule(
                "schedule1",
                FrequencyType.DAILY,
                2,
                startDate,
                LocalTime.of(1, 0),
                DivisionType.FIX,
                List.of(member.getId())
        );

        //when
        Long scheduleId = scheduleService.createSchedule(createSchedule, member, now);

        //then
        assertThat(scheduleId).isNotNull();

        List<Chore> chore = em.createQuery("select c from Chore c", Chore.class)
                .getResultList();

        assertThat(chore).hasSize(0);
    }

    private Member createMemberInHouse(House house) {

        Member member = Member.builder().email("email").password("password").build();
        member.enterHouse(house);
        em.persist(member);
        return member;
    }

    private House createHouse() {
        House house = House.of("house");
        em.persist(house);
        return house;
    }
}