package com.psp.paypalservice.util;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public interface DateUtil {
    // TODO mozda obrisati
    String ISO_DATE_FORMAT_ZERO_OFFSET = "yyyy-MM-dd'T'HH:mm:ss";
    String UTC_TIMEZONE_NAME = "UTC";

    static SimpleDateFormat provideDateFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ISO_DATE_FORMAT_ZERO_OFFSET);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(UTC_TIMEZONE_NAME));
        return simpleDateFormat;
    }
}