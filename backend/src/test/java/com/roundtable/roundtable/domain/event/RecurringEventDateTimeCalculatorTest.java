package com.roundtable.roundtable.domain.event;

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

class RecurringEventDateTimeCalculatorTest {

    @DisplayName("반복 날짜를 계산한다.")
    @ParameterizedTest
    @MethodSource("createRepetitionDatesTestData")
    void calculateRepetitionDates(LocalDateTime startDateTime, Repetition repetition, int count,
                                  List<LocalDateTime> expectedResult) {
        //given
        RecurringEventDateTimeCalculator recurringEventDateTimeCalculator = new RecurringEventDateTimeCalculator();

        //when
        List<LocalDateTime> dateTimes = recurringEventDateTimeCalculator.calculateRepetitionDates(
                startDateTime,
                repetition,
                count
        );

        //then
        assertThat(dateTimes).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> createRepetitionDatesTestData() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2024, 3, 1, 10, 0),
                        Repetition.builder()
                                .repetitionType(RepetitionType.WEEKLY)
                                .repeatCycle(1)
                                .repeatedUntilDate(LocalDate.of(2025, 3, 1))
                                .daysOfWeeks(List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY))
                                .build(),
                        5,
                        List.of(
                                LocalDateTime.of(2024, 3, 2, 10, 0),
                                LocalDateTime.of(2024, 3, 3, 10, 0),
                                LocalDateTime.of(2024, 3, 9, 10, 0),
                                LocalDateTime.of(2024, 3, 10, 10, 0),
                                LocalDateTime.of(2024, 3, 16, 10, 0)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 5, 2, 13, 0),
                        Repetition.builder()
                                .repetitionType(RepetitionType.DAILY)
                                .repeatCycle(3)
                                .repeatedUntilDate(LocalDate.of(2025, 3, 1))
                                .build(),
                        5,
                        List.of(
                                LocalDateTime.of(2024, 5, 2, 13, 0),
                                LocalDateTime.of(2024, 5, 5, 13, 0),
                                LocalDateTime.of(2024, 5, 8, 13, 0),
                                LocalDateTime.of(2024, 5, 11, 13, 0),
                                LocalDateTime.of(2024, 5, 14, 13, 0)
                        )
                ),
                Arguments.of(
                        LocalDateTime.of(2024, 10, 2, 19, 0),
                        Repetition.builder()
                                .repetitionType(RepetitionType.MONTHLY)
                                .repeatCycle(2)
                                .repeatedUntilDate(LocalDate.of(2025, 3, 1))
                                .build(),
                        5,
                        List.of(
                                LocalDateTime.of(2024, 10, 2, 19, 0),
                                LocalDateTime.of(2024, 12, 2, 19, 0),
                                LocalDateTime.of(2025, 2, 2, 19, 0)
                        )
                )
        );
    }
}