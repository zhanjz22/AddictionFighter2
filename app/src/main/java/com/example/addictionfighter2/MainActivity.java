package com.example.addictionfighter2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private NotificationManagerCompat nmc;

    @Override
    protected void onStop() {
        //Intent intent = new Intent(this, CurfewNotificationReceiver.class);
        //intent.putExtra("NotificationText", "some text");
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 2 /*doesnt matter*/, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, 5000, pendingIntent);
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_main);

        nmc = NotificationManagerCompat.from(this);

        Button buttonUsageStats = findViewById(R.id.button_usage_stats);
        buttonUsageStats.setOnClickListener(view -> {
            Intent usageStatsIntent = new Intent(getApplicationContext(), UsageStatsActivity.class);
            startActivity(usageStatsIntent);
        });

        Button buttonPlan = findViewById(R.id.button_plan);
        buttonPlan.setOnClickListener(view -> {
            Intent planCreate = new Intent(getApplicationContext(), SelectPlanActivity.class);
            startActivity(planCreate);
        });

        Button buttonNotify = findViewById(R.id.button_notify);
        buttonNotify.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
                    return;
                }
            }
            sendNotification();
        });

        Quotes quotes = new Quotes();
        String randomQuote = quotes.getQuote();

        // Find the TextView by ID and set the randomly selected message
        TextView motivationTextView = (TextView) findViewById(R.id.motivation);
        motivationTextView.setText(randomQuote);

        TextView planNameTextView = findViewById(R.id.planNameTextView);

        // Get the plan name sent from SelectPlanActivity
        String planName = getIntent().getStringExtra("selected_plan_name");
        if (planName != null) {
            planNameTextView.setText("Current plan: " + planName);
        } else {
            planNameTextView.setText("No plan selected");
        }

        Intent foregroundMonitoring = new Intent(getApplicationContext(), MonitoringService.class);
        getApplicationContext().startForegroundService(foregroundMonitoring);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendNotification();
            } else {
                Toast.makeText(this, "Notification permission is required to send notifications", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendNotification() {
        // Create an explicit intent for an Activity in your app.
        Intent intent = new Intent(this, UsageStatsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Make sure to use a valid icon
                .setContentTitle("WakeUp")
                .setContentText("You have turned notifications on!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        nmc.notify(0, builder.build()); //permission is already checked before sendNotification is called
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name); // You need to define this in your strings.xml
            String description = getString(R.string.channel_description); // Define this too
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void displayPlanDetails(Plan plan) {
        TextView planDetailsTextView = findViewById(R.id.planDetailsTextView); // No text view currently
        // Format and set the plan details to TextView
        planDetailsTextView.setText(formatPlanDetails(plan));
    }

    private String formatPlanDetails(Plan plan) {
        StringBuilder details = new StringBuilder();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        for (int i = 0; i < plan.timePerDay.length; i++) {
            if (plan.beginPerDay[i] != null) {
                details.append("Day ")
                        .append(i + 1)
                        .append(": ")
                        .append(plan.beginPerDay[i].format(dtf))
                        .append(" to ")
                        .append(plan.endPerDay[i].format(dtf))
                        .append(", ")
                        .append(plan.timePerDay[i] / 3600)
                        .append(" hrs\n");
            }
        }
        return details.toString();
    }
}
