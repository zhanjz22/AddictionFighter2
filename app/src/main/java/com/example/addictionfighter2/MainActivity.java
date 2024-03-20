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
import android.provider.Settings;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
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

        Button buttonNotify = findViewById(R.id.button_notify);

        // Create an explicit intent for an Activity in your app.
        Intent intent = new Intent(this, UsageStatsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "test_channel")
                //.setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that fires when the user taps the notification.
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);

        buttonNotify.setOnClickListener(view -> {
            nmc.notify(0, builder.build());
        });

        startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Testing";
            String description = "For testing of notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("test_channel", name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.enableVibration(true);
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}