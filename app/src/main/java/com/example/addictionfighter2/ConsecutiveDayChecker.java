package com.example.addictionfighter2;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

class ConsecutiveDayChecker {

    public ConsecutiveDayChecker(Context context) {
        this.context = context;

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        Date date = new Date();
        String today = dateFormat.format(date);
        String lastLoginDay = getLastLoginDate();

        String yesterday = getYesterdayDate(dateFormat, date);

        if (lastLoginDay == null) {
            // user logged in for the first time
            updateLastLoginDate(today);
            incrementDays();
        } else {
            if (lastLoginDay.equals(today)) {
                // User logged in the same day , do nothing
            } else if (lastLoginDay.equals(yesterday)) {
                // User logged in consecutive days , add 1
                updateLastLoginDate(today);
                incrementDays();
            } else {
                // It's been more than a day user logged in, reset the counter to 1
                updateLastLoginDate(today);
                resetDays();
            }
        }
    }

    private String getYesterdayDate(DateFormat simpleDateFormat, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        return simpleDateFormat.format(calendar.getTime());
    }

    private void updateLastLoginDate(String date) {
        SharedPreferences sharedPref = context.getSharedPreferences("streak", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("last_login_day", date);
        editor.apply();
    }

    private String getLastLoginDate() {
        String lastLogin = null;
        SharedPreferences sharedPref = context.getSharedPreferences("streak", Context.MODE_PRIVATE);
        lastLogin = sharedPref.getString("last_login_day", null);
        return lastLogin;
    }

    private int getConsecutiveDays() {
        int days = 0;
        SharedPreferences sharedPref = context.getSharedPreferences("streak", Context.MODE_PRIVATE);
        days = sharedPref.getInt("num_consecutive_days", 0);
        return days;
    }

    private void incrementDays() {
        int days = getConsecutiveDays() + 1;
        SharedPreferences sharedPref = context.getSharedPreferences("streak", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("num_consecutive_days", days);
        editor.apply();
    }

    private void resetDays() {
        int days = 1;
        SharedPreferences sharedPref = context.getSharedPreferences("streak", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("num_consecutive_days", days);
        editor.apply();
    }

    public int getStreak() {
        return getConsecutiveDays();
    }

    Context context;
}