package com.roundtable.roundtable.business.schedule.dto;

import java.time.LocalDate;

public record DateScheduleCountResponse(
        LocalDate startDate,
        Long count
) {
}
