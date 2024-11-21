package com.roundtable.roundtable.domain.event.adjuster;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MonthlyStartDateTimeAdjusterTest {

    @DisplayName("시작날짜가 현재날짜 이전이라면 다음달에 해당하는 요일로 조정한다.")
    @ParameterizedTest
    @CsvSource({
            "2024-10-22T10:00, 2024-10-31, 2024-11-22T10:00", // 시작 날짜가 현재 날짜 이전인 경우
            "2024-11-22T10:00, 2024-10-31, 2024-11-22T10:00", // 시작 날짜가 이미 현재 날짜 이후인 경우
            "2024-09-15T10:00, 2024-09-30, 2024-10-15T10:00"  // 시작 날짜가 다른 달로 이동하는 경우
    })
    void adjustStartDateTime(LocalDateTime startDateTime, LocalDate now, LocalDateTime expectedDateTime) {
        //given
        MonthlyStartDateTimeAdjuster monthlyStartDateTimeAdjuster = new MonthlyStartDateTimeAdjuster(now,
                startDateTime);

        //when
        LocalDateTime adjustedStartDateTime = monthlyStartDateTimeAdjuster.adjustStartDateTime();

        //then
        assertThat(adjustedStartDateTime).isEqualTo(expectedDateTime);
    }
}