package com.example.addictionfighter2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonUsageStats = findViewById(
                R.id.button_usage_stats
        );
        buttonUsageStats.setOnClickListener(view -> {
            Intent usageStatsIntent = new Intent(
                    getApplicationContext(), UsageStatsActivity.class
            );
            startActivity(usageStatsIntent);
        });

    }


}