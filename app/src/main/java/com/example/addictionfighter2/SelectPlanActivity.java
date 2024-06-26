package com.example.addictionfighter2;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class SelectPlanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_plan);

        // Setting up button listeners
        findViewById(R.id.button_cold).setOnClickListener(view -> createPlan("cold"));
        findViewById(R.id.button_reduced).setOnClickListener(view -> createPlan("reduced"));
        findViewById(R.id.button_school_focus).setOnClickListener(view -> createPlan("schoolFocus"));
        findViewById(R.id.button_sleep).setOnClickListener(view -> createPlan("sleep"));
        findViewById(R.id.button_create).setOnClickListener(view -> {
            // Redirect to another activity where the user can create a custom plan
            Intent intent = new Intent(this, CustomPlanActivity.class);
            startActivity(intent);
        });

        Spinner appSpinner = findViewById(R.id.app_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.app_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appSpinner.setAdapter(adapter);

        // Get the selected app name from UsageStatsActivity
        String selectedAppName = getIntent().getStringExtra("PACKAGE_NAME");
        if (selectedAppName != null && !selectedAppName.isEmpty()) {
            int spinnerPosition = adapter.getPosition(selectedAppName);
            appSpinner.setSelection(spinnerPosition);
        }
    }

    private List<String> getAppNames() {
        List<String> appNames = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;  // Skip system apps

            String label = packageManager.getApplicationLabel(app).toString();
            appNames.add(label);
        }
        return appNames;
    }



    private void createPlan(String type) {
        Intent intent = new Intent(SelectPlanActivity.this, MainActivity.class);
        Plan plan = new Plan();
        switch (type) {
            case "cold":
                // No usage allowed at all
                plan.setAllTimes(0);
                plan.setName("Cold Turkey");
                break;
            case "reduced":
                // Reduced plan: 30 minutes per day
                plan.setAllTimes(1800);
                plan.setName("Reduced");
                break;
            case "schoolFocus":
                // School focus: Allowed only on weekends
                plan.setDayTime(DayOfWeek.SATURDAY, 86400); // Full day in seconds
                plan.setDayTime(DayOfWeek.SUNDAY, 86400);
                plan.setName("School Focus");
                break;
            case "sleep":
                // Sleep plan: 1 hour per day, not after 9 PM
                plan.setAllTimes(3600);
                plan.setAllEnds(LocalTime.of(21, 0)); // 9 PM
                plan.setName("Sleep");
                break;
            default: plan.setName("Custom");
        }
        intent.putExtra("selected_plan_name", plan.getName()); // Assuming selectedPlan is your Plan object
        startActivity(intent);
        finish(); // Optional, if you want to close the select plan activity

    }
}

