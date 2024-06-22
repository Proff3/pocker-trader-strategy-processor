package org.surfer.strategyprocessor.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TimeZone {

    RUSSIA(7, 0, 15, 40),
    USA(13, 30, 20, 0),
    ;

    private final int startHours;
    private final int startMinutes;
    private final int endHours;
    private final int endMinutes;
}
