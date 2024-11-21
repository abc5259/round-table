package com.roundtable.roundtable.domain.event.adjuster;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DailyStartDateTimeAdjusterTest {

    @DisplayName("시작날짜가 현재날짜보다 이전이라면 현재날짜로 조정한다.")
    @ParameterizedTest
    @CsvSource({
            "2024-10-22T10:00, 2024-11-22, 2024-11-22T10:00", // 시작 날짜가 이전일 때
            "2024-11-22T10:00, 2024-11-22, 2024-11-22T10:00", // 시작 날짜가 같은 날짜일 때
            "2024-12-01T10:00, 2024-11-22, 2024-12-01T10:00"  // 시작 날짜가 이후일 때
    })
    void adjustStartDateTime(LocalDateTime startDateTime, LocalDate now, LocalDateTime expectedDateTime) {
        //given
        DailyStartDateTimeAdjuster dailyStartDateTimeAdjuster = new DailyStartDateTimeAdjuster(now, startDateTime);

        //when
        LocalDateTime adjustedStartDateTime = dailyStartDateTimeAdjuster.adjustStartDateTime();

        //then
        assertThat(adjustedStartDateTime).isEqualTo(expectedDateTime);
    }
}