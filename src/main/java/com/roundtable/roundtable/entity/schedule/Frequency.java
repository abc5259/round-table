package com.roundtable.roundtable.entity.schedule;

import static com.roundtable.roundtable.entity.schedule.FrequencyType.*;

import com.roundtable.roundtable.entity.schedule.ScheduleException.CreateScheduleException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Frequency {
    @Enumerated(EnumType.STRING)
    private FrequencyType frequencyType;

    @Column(nullable = false)
    private Integer frequencyInterval;

    private Frequency(FrequencyType frequencyType, Integer frequencyInterval) {
        this.frequencyType = frequencyType;
        this.frequencyInterval = frequencyInterval;
    }

    public static Frequency of(FrequencyType frequencyType, Integer frequencyInterval) {

        if(!validate(frequencyType, frequencyInterval)) {
            throw new CreateScheduleException("frequencyType에 맞는 frequencyInterval값이 아닙니다.");
        }

        return new Frequency(frequencyType, frequencyInterval);

    }

    private static boolean validate(FrequencyType frequencyType, Integer frequencyInterval) {
        if(frequencyInterval < 0)
            return false;

        if(frequencyType.equals(ONCE)) {
            return frequencyInterval == 0;
        }

        if(frequencyType.equals(DAILY)) {
            return frequencyInterval >= 1;
        }

        if(frequencyType.equals(WEEKLY)) {
            return frequencyInterval >= 1 && frequencyInterval <= 7;
        }

        return true;
    }
}
