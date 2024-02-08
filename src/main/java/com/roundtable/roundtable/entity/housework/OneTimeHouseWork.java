package com.roundtable.roundtable.entity.housework;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OneTimeHouseWork extends HouseWork {
    public OneTimeHouseWork(String name, HouseWorkCategory houseWorkCategory, Integer currSequence,
                            Integer sequenceSize,
                            LocalDate activeDate, LocalDate deActiveDate,
                            LocalTime assignedTime) {
        super(name, houseWorkCategory, currSequence, sequenceSize, activeDate, deActiveDate, assignedTime);
    }
}
