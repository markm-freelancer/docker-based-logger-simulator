package com.dbls.impl.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public static LocalDateTime parseDateTime(String timeStr) {
        LocalTime time = LocalTime.parse(timeStr, FORMATTER);
        LocalDateTime date = LocalDateTime.now().with(time);
        return date;
    }

    public static String readableTime(LocalDateTime time) {
        return time.format(FORMATTER);
    }

}
