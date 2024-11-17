package com.roundtable.roundtable.domain.event;

public enum RepetitionType {
    DAILY(100),
    WEEKLY(10),
    MONTHLY(12);

    private final int maxRepeatCycle;

    RepetitionType(int maxRepeatCycle) {
        this.maxRepeatCycle = maxRepeatCycle;
    }

    public int calculateMinRepeatCycle(int repeatCycle) {
        return Math.min(repeatCycle, maxRepeatCycle);
    }
}

