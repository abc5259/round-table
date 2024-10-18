package com.roundtable.roundtable.presentation.schedule.request;

import com.roundtable.roundtable.business.schedule.dto.CreateOneTimeScheduleDto;
import com.roundtable.roundtable.business.schedule.dto.CreateScheduleDto;
import com.roundtable.roundtable.domain.schedule.Category;
import com.roundtable.roundtable.domain.schedule.Day;
import com.roundtable.roundtable.domain.schedule.DivisionType;
import com.roundtable.roundtable.domain.schedule.ScheduleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.hibernate.validator.constraints.UniqueElements;

public record CreateOneTimeScheduleRequest(
        @NotBlank(message = "name에 빈 값이 올 수 없습니다.")
        String name,

        @NotNull(message = "startDate에 빈 값이 올 수 없습니다.")
        LocalDate startDate,

        @NotNull(message = "startTime에 빈 값이 올 수 없습니다.")
        LocalTime startTime,

        @Size(min = 1, max = 30, message = "담당자는 최소 1명 최대 30명까지 가능합니다.")
        List<Long> memberIds
) {
    public CreateOneTimeScheduleDto toCreateScheduleDto() {
        return new CreateOneTimeScheduleDto(
                name,
                startDate,
                startTime,
                memberIds
        );
    }
}
