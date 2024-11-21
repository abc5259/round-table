package com.roundtable.roundtable.domain.event.adjuster;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class WeeklyStartDateTimeAdjusterTest {

    @DisplayName("시작 날짜를 현재 날짜 이후 가장 가까운 요일로 조정한다.")
    @ParameterizedTest
    @MethodSource("createAdjustStartDateTimeData")
    void adjustStartDateTime(LocalDateTime startDateTime,
                             LocalDate now,
                             List<DayOfWeek> daysOfWeek,
                             LocalDateTime expectedDateTime) {
        //given
        WeeklyStartDateTimeAdjuster adjuster = new WeeklyStartDateTimeAdjuster(now, startDateTime, daysOfWeek);

        //when
        LocalDateTime adjustedDateTime = adjuster.adjustStartDateTime();

        //then
        assertThat(adjustedDateTime).isEqualTo(expectedDateTime);
    }

    private static Stream<Arguments> createAdjustStartDateTimeData() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2024, 10, 22, 10, 0),
                        LocalDate.of(2024, 10, 31),
                        List.of(MONDAY, TUESDAY, WEDNESDAY),
                        LocalDateTime.of(2024, 11, 4, 10, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 22, 10, 0),
                        LocalDate.of(2024, 10, 20),
                        List.of(MONDAY, FRIDAY),
                        LocalDateTime.of(2024, 10, 25, 10, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 20, 10, 0),
                        LocalDate.of(2024, 10, 25),
                        List.of(WEDNESDAY, FRIDAY),
                        LocalDateTime.of(2024, 10, 25, 10, 0)
                )
        );
    }
}