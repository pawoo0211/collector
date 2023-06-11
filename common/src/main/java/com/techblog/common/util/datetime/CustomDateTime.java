package com.techblog.common.util.datetime;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTime {

    public static LocalDateTime toLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTimeString, formatter);
        LocalDateTime dateTime = offsetDateTime.toLocalDateTime();

        return dateTime;
    }
}