package com.example.addictionfighter2;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;


public class Plan {
    public Plan() {
        timePerDay = new long[7];
        beginPerDay = new LocalTime[7];
        endPerDay = new LocalTime[7];
    }

    public void setDayTime(DayOfWeek day, long time) {
        timePerDay[day.getValue() % 7] = time;
    }

    public void setDayBegin(DayOfWeek day, LocalTime time) {
        beginPerDay[day.getValue() % 7] = time;
    }

    public void setDayEnd(DayOfWeek day, LocalTime time) {
        endPerDay[day.getValue() % 7] = time;
    }

    public void setAllTimes(long time) {
        for (int i = 0; i < 7; ++i)
            timePerDay[i] = time;
    }

    public void setAllBegins(LocalTime time) {
        for (int i = 0; i < 7; ++i)
            beginPerDay[i] = time;
    }

    public void setAllEnds(LocalTime time) {
        for (int i = 0; i < 7; ++i)
            endPerDay[i] = time;
    }

    public long getMaxTime(DayOfWeek day) {
        return timePerDay[day.getValue() % 7];
    }

    public LocalTime getBegin(DayOfWeek day) {
        return beginPerDay[day.getValue() % 7];
    }

    public LocalTime getEnd(DayOfWeek day) {
        return endPerDay[day.getValue() % 7];
    }

    public boolean okToUse(LocalDateTime dt) {
        DayOfWeek day = dt.getDayOfWeek();
        LocalTime t = dt.toLocalTime();

        if (t.isBefore(beginPerDay[day.getValue() % 7]))
            return false;

        if (t.isAfter(endPerDay[day.getValue() % 7]))
            return false;

        return true;
    }

    long[] timePerDay; //in seconds per day
    LocalTime[] beginPerDay;
    LocalTime[] endPerDay;
}
