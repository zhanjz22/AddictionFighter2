package com.example.addictionfighter2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ArrayAdapter;
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

        savePlanButton.setOnClickListener(view -> savePlan());
    }

    private void savePlan() {
        DayOfWeek day = DayOfWeek.valueOf(daySpinner.getSelectedItem().toString().toUpperCase());
        // Convert input from minutes to seconds
        long timeLimitSeconds = Long.parseLong(timeLimitInput.getText().toString()) * 60;
        LocalTime startTime = LocalTime.of(startTimePicker.getHour(), startTimePicker.getMinute());
        LocalTime endTime = LocalTime.of(endTimePicker.getHour(), endTimePicker.getMinute());

        Plan plan = new Plan();
        plan.setDayTime(day, timeLimitSeconds);
        plan.setDayBegin(day, startTime);
        plan.setDayEnd(day, endTime);

        // Optionally, you can send this Plan object to another activity or use it directly
    }
}

