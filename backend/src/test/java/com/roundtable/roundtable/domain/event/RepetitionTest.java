package com.roundtable.roundtable.domain.event;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RepetitionTest {

    @DisplayName("Repetition 생성시 repeatCycle과 repeatedUntilDate의 최댓값보다 크게 생성할 수 없다.")
    @Test
    void create() {
        //given
        int repeatCycle = RepetitionType.DAILY.getMaxRepeatCycle() + 1;
        LocalDate repeatedUntilDate = LocalDate.of(2100, 1, 1);

        //when
        Repetition repetition = Repetition.builder()
                .repetitionType(RepetitionType.DAILY)
                .repeatCycle(repeatCycle)
                .repeatedUntilDate(repeatedUntilDate)
                .build();

        //then
        assertThat(repetition.getRepeatCycle()).isEqualTo(RepetitionType.DAILY.getMaxRepeatCycle());
        assertThat(repetition.getRepeatedUntilDate()).isEqualTo(Repetition.MAX_REPEATED_UNTIL_DATE);
    }

    @DisplayName("repeatCycle이 1보다 작다면 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource({"-100", "-1", "0"})
    void repeatCycleLessThanOneThrowException(int repeatCycle) {
        assertThatThrownBy(() -> Repetition.builder().repeatCycle(repeatCycle).build())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("반복 주기는 1보다 크거나 같아야 합니다.");
    }
}