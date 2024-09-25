package ui.components;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class TimeCalculator {
    // Calculates and returns the time elapsed since a given time, formatted as a string
    protected static String timeSince(String timeSince) {
        LocalDateTime formattedTime = LocalDateTime.parse(timeSince, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime currentTime = LocalDateTime.now();
        long minutes = ChronoUnit.MINUTES.between(formattedTime, currentTime);

        // Return the time difference in a suitable format (minutes, hours, days)
        if (minutes < 60) {
            return STR."\{minutes} minutes";
        } else {
            long hours = ChronoUnit.HOURS.between(formattedTime, currentTime);
            if (hours < 24) {
                return STR."\{hours} hours";
            } else {
                long days = ChronoUnit.DAYS.between(formattedTime, currentTime);
                return STR."\{days} days";
            }
        }
    }
}

