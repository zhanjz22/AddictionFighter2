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
import android.widget.TextView;


import java.util.Random;
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

        Button buttonPlan = findViewById(
                R.id.button_plan
        );
        buttonPlan.setOnClickListener(view -> {
            Intent planCreate = new Intent(
                    getApplicationContext(), PlanCreate.class
            );
            startActivity(planCreate);
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

        // Array of custom messages
        String[] customMessages = {
                "Believe in change, it starts now",
                "Embrace your strength, defeat your struggles",
                "One step at a time moves mountains",
                "Break free, your spirit deserves peace",
                "Find strength in every challenge",
                "Courage is not the absence of fear",
                "Hope is stronger than addiction",
                "Let each day be a fresh start",
                "Change is possible, believe in yourself",
                "Overcome, rise, and shine",
                "Freedom from addiction is a choice",
                "Claim your life back, step by step",
                "Your will is stronger than your cravings",
                "Embrace the struggle, cherish the victory",
                "Let go of what holds you back",
                "Seek peace, not escape",
                "Today's efforts are tomorrow's rewards",
                "Your journey, your pace, your victory",
                "Transform your obstacles into stepping stones",
                "Rise above, one decision at a time",
                "Believe you can and you're halfway there.",
                "You are stronger than you think.",
                "Dream big and dare to fail.",
                "Strive for progress, not perfection.",
                "The only way to do great work is to love what you do.",
                "Success is not final, failure is not fatal: It is the courage to continue that counts.",
                "In the middle of every difficulty lies opportunity.",
                "Don't wait for opportunity, create it.",
                "Your limitation—it's only your imagination.",
                "The harder you work for something, the greater you'll feel when you achieve it.",
                "Wake up with determination. Go to bed with satisfaction.",
                "The only person you should try to be better than is the person you were yesterday.",
                "You don't have to be great to start, but you have to start to be great.",
                "The future belongs to those who believe in the beauty of their dreams.",
                "Believe you deserve it and the universe will serve it."
        };

        // Randomly select one of the messages
        Random random = new Random();
        int index = random.nextInt(customMessages.length);

        // Find the TextView by ID and set the randomly selected message
        TextView motivationTextView = (TextView) findViewById(R.id.motivation);
        motivationTextView.setText(customMessages[index]);

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