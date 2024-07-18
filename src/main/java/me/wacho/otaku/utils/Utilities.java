package me.wacho.otaku.utils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Utilities {

    public static String getCurrentFormattedDate() {
        LocalDateTime serverDateTime = LocalDateTime.now();
        ZoneId playerZone = getPlayerZone();

        LocalDateTime playerDateTime = serverDateTime.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(playerZone)
                .toLocalDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.US);
        return formatter.format(playerDateTime);
    }

    public static String getFormatNumber(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(number);
    }

    private static ZoneId getPlayerZone() {
        return ZoneId.systemDefault();
    }
}
