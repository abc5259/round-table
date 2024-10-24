package com.roundtable.roundtable.domain.schedule.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDate;

public record DateScheduleCountDto(
        LocalDate startDate,
        Long count
) {

    @QueryProjection
    public DateScheduleCountDto {
    }
}
