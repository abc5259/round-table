package com.roundtable.roundtable.domain.event;

import java.time.DayOfWeek;
import lombok.Getter;

@Getter
public enum Day {
    MON(1),
    TUE(2),
    WED(3),
    THU(4),
    FRI(5),
    SAT(6),
    SUN(7);

    private final int id;

    Day(int id) {
        this.id = id;
    }

    public static Day fromId(int id) {
        return switch (id) {
            case 1 -> MON;
            case 2 -> TUE;
            case 3 -> WED;
            case 4 -> THU;
            case 5 -> FRI;
            case 6 -> SAT;
            case 7 -> SUN;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    public static Day forDayOfWeek(DayOfWeek dayOfWeek) {
        return switch (dayOfWeek) {
            case MONDAY -> MON;
            case TUESDAY -> TUE;
            case WEDNESDAY -> WED;
            case THURSDAY -> THU;
            case FRIDAY -> FRI;
            case SATURDAY -> SAT;
            case SUNDAY -> SUN;
        };
    }
}
