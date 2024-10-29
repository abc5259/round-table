package com.roundtable.roundtable.domain.event;

import java.time.LocalDateTime;

public interface StartDateTimeAdjuster {
    LocalDateTime adjustStartDateTime();
}
