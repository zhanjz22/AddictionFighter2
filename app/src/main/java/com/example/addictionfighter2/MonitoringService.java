package com.example.addictionfighter2;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.ServiceCompat;

public class MonitoringService extends Service {
    private NotificationManager mNM;
    private Handler handler;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private final int NOTIFICATION = 123;

    Thread runner;

    static boolean kill = false;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        MonitoringService getService() {
            return MonitoringService.this;
        }
    }

    @Override
    public void onCreate() {
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        createNotificationChannel();

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);
        Notification existenceNotification = new Notification.Builder(this, "monitoring_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setTicker("Monitoring service")  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Wake Up")  // the label of the entry
                .setContentText("Monitors your app usage")  // the contents of the entry
                //.setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        startForeground(NOTIFICATION, existenceNotification, ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE);

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                // TODO Auto-generated method stub
                super.handleMessage(msg);
                //if (msg.)
                if (msg.what == 0)
                    //Toast.makeText(getApplicationContext(), "5 secs has passed", Toast.LENGTH_SHORT).show();
                    ;
                else if (msg.what == 1)
                    Toast.makeText(getApplicationContext(), "Thread dying", Toast.LENGTH_SHORT).show();
                else if (msg.what == 2)
                    Toast.makeText(getApplicationContext(),
                            "You've spent too long in " + (String)msg.obj, Toast.LENGTH_LONG).show();
            }
        };

        runner = new Thread(new Runnable(){
            public void run() {
                // TODO Auto-generated method stub
                while(true)
                {
                    try {
                        Thread.sleep(30000);
                        ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
                        // The first in the list of RunningTasks is always the foreground task.
                        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                        String foregroundTaskPackageName = foregroundTaskInfo.topActivity.getPackageName();
                        PackageManager pm = getApplicationContext().getPackageManager();
                        try {
                            PackageInfo foregroundAppPackageInfo = pm.getPackageInfo(foregroundTaskPackageName, 0);
                            String foregroundTaskAppName = foregroundAppPackageInfo.applicationInfo.loadLabel(pm).toString();
                            Message msg = new Message();
                            msg.obj = foregroundTaskAppName;
                            msg.what = 2;
                            handler.sendMessage(msg);
                        }
                        catch (PackageManager.NameNotFoundException nne) {
                            Message msg = new Message();
                            msg.obj = foregroundTaskPackageName;
                            msg.what = 2;
                            handler.sendMessage(msg);
                            handler.sendEmptyMessage(0);
                        }
                        //handler.sendEmptyMessage(0);
                        if (kill) break;
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                handler.sendEmptyMessage(1);

            }
        });

        runner.start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        //mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        kill = true;
        Toast.makeText(this, "Monitoring stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = "Monitoring in progress";

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);

        // Set the info for the views that show in the notification panel.
        Notification notification = new Notification.Builder(this)
                //.setSmallIcon()  // the status icon
                .setTicker(text)  // the status text
                .setWhen(System.currentTimeMillis())  // the time stamp
                .setContentTitle("Wake Up")  // the label of the entry
                .setContentText(text)  // the contents of the entry
                .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                .build();

        // Send the notification.
        mNM.notify(NOTIFICATION, notification);
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name); // You need to define this in your strings.xml
        String description = getString(R.string.channel_description); // Define this too
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("monitoring_channel", name, importance);
        channel.setDescription(description);
        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}