package com.roundtable.roundtable.domain.schedule;

import static org.assertj.core.api.Assertions.*;

import com.roundtable.roundtable.domain.house.House;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @DisplayName("반스케줄을 생성할때 sequence값은 0이다.")
    @Test
    void create_repeat_schedule() {
        //given
        String name = "schedule";
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalTime startTime = LocalTime.of(12, 00);
        DivisionType divisionType = DivisionType.ROTATION;
        House house = House.Id(1L);
        int sequenceSize = 2;
        Category category = Category.COOKING;

        //when
        Schedule schedule = Schedule.creatRepeatSchedule(name, startDate, startTime, divisionType, house, sequenceSize, category);

        //thenR
        assertThat(schedule.getSequence()).isEqualTo(0);
        assertThat(schedule.getSequenceSize()).isEqualTo(2);
    }


    @DisplayName("스케줄을 생성할때 분담방식이 고정이라면 sequenceSize는 무조건 1이다.")
    @Test
    void create_fix_schedule_sequenceSize_is_one() {
        //given
        String name = "schedule";
        LocalDate startDate = LocalDate.of(2020, 1, 1);
        LocalTime startTime = LocalTime.of(12, 00);
        DivisionType divisionType = DivisionType.FIX;
        House house = House.Id(1L);
        int sequenceSize = 12;
        Category category = Category.COOKING;

        //when
        Schedule schedule = Schedule.creatRepeatSchedule(name, startDate, startTime, divisionType, house, sequenceSize, category);

        //thenR
        assertThat(schedule.getSequenceSize()).isEqualTo(1);
    }
}