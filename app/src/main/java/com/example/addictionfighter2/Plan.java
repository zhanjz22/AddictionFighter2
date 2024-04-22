package com.example.addictionfighter2;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Plan implements Parcelable {
    long[] timePerDay; // Time in seconds per day
    LocalTime[] beginPerDay; // Start time per day
    LocalTime[] endPerDay; // End time per day

    public Plan() {
        timePerDay = new long[7];
        beginPerDay = new LocalTime[7];
        endPerDay = new LocalTime[7];
    }

    protected Plan(Parcel in) {
        timePerDay = in.createLongArray();
        beginPerDay = new LocalTime[7];
        endPerDay = new LocalTime[7];
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_TIME;
        for (int i = 0; i < 7; i++) {
            String beginTime = in.readString();
            String endTime = in.readString();
            beginPerDay[i] = beginTime.isEmpty() ? null : LocalTime.parse(beginTime, dtf);
            endPerDay[i] = endTime.isEmpty() ? null : LocalTime.parse(endTime, dtf);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLongArray(timePerDay);
        DateTimeFormatter dtf = DateTimeFormatter.ISO_LOCAL_TIME;
        for (int i = 0; i < 7; i++) {
            dest.writeString(beginPerDay[i] == null ? "" : beginPerDay[i].format(dtf));
            dest.writeString(endPerDay[i] == null ? "" : endPerDay[i].format(dtf));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Plan> CREATOR = new Creator<Plan>() {
        @Override
        public Plan createFromParcel(Parcel in) {
            return new Plan(in);
        }

        @Override
        public Plan[] newArray(int size) {
            return new Plan[size];
        }
    };

    // Sets time allowed per day for a specific day
    public void setDayTime(DayOfWeek day, long time) {
        timePerDay[day.getValue() - 1] = time;
    }

    // Sets the begin time per day for a specific day
    public void setDayBegin(DayOfWeek day, LocalTime time) {
        beginPerDay[day.getValue() - 1] = time;
    }

    // Sets the end time per day for a specific day
    public void setDayEnd(DayOfWeek day, LocalTime time) {
        endPerDay[day.getValue() - 1] = time;
    }

    // Sets the same time for all days
    public void setAllTimes(long time) {
        for (int i = 0; i < 7; i++) {
            timePerDay[i] = time;
        }
    }

    // Sets the same begin time for all days
    public void setAllBegins(LocalTime time) {
        for (int i = 0; i < 7; i++) {
            beginPerDay[i] = time;
        }
    }

    // Sets the same end time for all days
    public void setAllEnds(LocalTime time) {
        for (int i = 0; i < 7; i++) {
            endPerDay[i] = time;
        }
    }

    // Gets the maximum allowed time for a specific day
    public long getMaxTime(DayOfWeek day) {
        return timePerDay[day.getValue() - 1];
    }

    // Gets the begin time for a specific day
    public LocalTime getBegin(DayOfWeek day) {
        return beginPerDay[day.getValue() - 1];
    }

    // Gets the end time for a specific day
    public LocalTime getEnd(DayOfWeek day) {
        return endPerDay[day.getValue() - 1];
    }

    // Checks if usage is ok at a specific datetime
    public boolean okToUse(LocalTime dt, DayOfWeek day) {
        LocalTime begin = beginPerDay[day.getValue() - 1];
        LocalTime end = endPerDay[day.getValue() - 1];
        return !dt.isBefore(begin) && !dt.isAfter(end);
    }
}
