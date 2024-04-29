package com.example.addictionfighter2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.time.DayOfWeek;
import java.time.LocalTime;

public class CustomPlanActivity extends AppCompatActivity {

    private Spinner daySpinner;
    private EditText timeLimitInput;
    private TimePicker startTimePicker;
    private TimePicker endTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_plan);

        daySpinner = findViewById(R.id.day_spinner);
        timeLimitInput = findViewById(R.id.time_limit_input);
        startTimePicker = findViewById(R.id.start_time_picker);
        endTimePicker = findViewById(R.id.end_time_picker);
        Button savePlanButton = findViewById(R.id.save_plan_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(adapter);

        savePlanButton.setOnClickListener(view -> {
            savePlan();
        });
    }

    private void savePlan() {
        String timeLimitStr = timeLimitInput.getText().toString();
        if (timeLimitStr.isEmpty()) {
            Toast.makeText(this, "Please enter a time limit", Toast.LENGTH_SHORT).show();
            return; // Early return if the input is invalid
        }

        long timeLimitSeconds = Long.parseLong(timeLimitStr) * 60;
        LocalTime startTime = LocalTime.of(startTimePicker.getHour(), startTimePicker.getMinute());
        LocalTime endTime = LocalTime.of(endTimePicker.getHour(), endTimePicker.getMinute());

        Plan plan = new Plan();
        plan.setName("Custom");

        String selectedDay = daySpinner.getSelectedItem().toString().toUpperCase();
        switch (selectedDay) {
            case "ALL":
                plan.setAllTimes(timeLimitSeconds);
                plan.setAllBegins(startTime);
                plan.setAllEnds(endTime);
                break;
            case "WEEKDAYS":
                for (DayOfWeek day : DayOfWeek.values()) {
                    if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                        plan.setDayTime(day, timeLimitSeconds);
                        plan.setDayBegin(day, startTime);
                        plan.setDayEnd(day, endTime);
                    }
                }
                break;
            case "WEEKENDS":
                plan.setDayTime(DayOfWeek.SATURDAY, timeLimitSeconds);
                plan.setDayBegin(DayOfWeek.SATURDAY, startTime);
                plan.setDayEnd(DayOfWeek.SATURDAY, endTime);
                plan.setDayTime(DayOfWeek.SUNDAY, timeLimitSeconds);
                plan.setDayBegin(DayOfWeek.SUNDAY, startTime);
                plan.setDayEnd(DayOfWeek.SUNDAY, endTime);
                break;
            default:
                DayOfWeek day = DayOfWeek.valueOf(selectedDay);
                plan.setDayTime(day, timeLimitSeconds);
                plan.setDayBegin(day, startTime);
                plan.setDayEnd(day, endTime);
                break;
        }

        // Transition back to MainActivity
        Intent intent = new Intent(CustomPlanActivity.this, MainActivity.class);
        intent.putExtra("selected_plan_name", plan.getName());
        //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish(); // Close the current activity
    }

}

