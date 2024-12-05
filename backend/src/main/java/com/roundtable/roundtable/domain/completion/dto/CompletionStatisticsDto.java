package com.roundtable.roundtable.domain.completion.dto;

import com.roundtable.roundtable.domain.event.Category;

public record CompletionStatisticsDto(
        Category category,
        Long completionCount
) {
}
