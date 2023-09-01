package io.aycodes.automataapi.common.utility;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class TimeUtil {

    private final static ZoneId ZONE_ID = ZoneId.of("Africa/Lagos");

    public static Date getDateOfInstant() {
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, ZONE_ID);
        Instant instant = zonedDateTime.toInstant();
        return Date.from(instant);
    }

    public static LocalDate convertDateToLocalDate(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZONE_ID)
                .toLocalDate();
    }

    public static ZonedDateTime convertDateToZonedDateTime(Date dateToConvert) {
        return Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZONE_ID)
                .withEarlierOffsetAtOverlap();
    }

    public static LocalDate getLocalDateOfInstant() {
        Instant now = Instant.now();
        return ZonedDateTime.ofInstant(now, ZONE_ID).toLocalDate();
    }

    public static ZonedDateTime getZonedDateTimeOfInstant() {
        Instant now = Instant.now();
        return ZonedDateTime.ofInstant(now, ZONE_ID);
    }

    public static String getFormattedDateTimeOfInstant() {
        Instant now = Instant.now();
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(now, ZONE_ID);
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy 'at' hh:mm a").format(zonedDateTime);
    }

    public static String formatThisZoneDateTime(final ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy 'at' hh:mm a").format(zonedDateTime);
    }
    public static String formatThisZoneDateTimeWithoutTime(final ZonedDateTime zonedDateTime) {
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy").format(zonedDateTime);
    }
    public static String formatThisDateWithoutTime(final Date dateToConvert) {
        ZonedDateTime zonedDateTime = Instant.ofEpochMilli(dateToConvert.getTime())
                .atZone(ZONE_ID)
                .withEarlierOffsetAtOverlap();
        return DateTimeFormatter.ofPattern("EEE, dd MMM yyyy").format(zonedDateTime);
    }

    public static ZoneId getZONE_ID() {
        return ZONE_ID;
    }
}
