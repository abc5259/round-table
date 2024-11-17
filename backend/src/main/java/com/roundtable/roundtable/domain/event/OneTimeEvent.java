package com.roundtable.roundtable.domain.event;

import com.roundtable.roundtable.domain.house.House;
import com.roundtable.roundtable.domain.member.Member;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OneTimeEvent extends Event {

    public OneTimeEvent(
            String name,
            Category category,
            LocalDateTime startDateTime,
            House house,
            Member creator) {
        super(
                name, category,
                startDateTime,
                null, house, creator);
    }

    public static OneTimeEvent from(String name,
                                    Category category,
                                    LocalDateTime startDateTime,
                                    House house,
                                    Member creator) {
        return new OneTimeEvent(name, category, startDateTime, house, creator);
    }
}
