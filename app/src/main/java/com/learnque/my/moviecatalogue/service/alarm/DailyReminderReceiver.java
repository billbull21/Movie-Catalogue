package com.learnque.my.moviecatalogue.service.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.learnque.my.moviecatalogue.R;

import java.util.Calendar;

public class DailyReminderReceiver extends BroadcastReceiver {

    public static final String TYPE_REMINDER = "Movie Catalogue";
    public static final String EXTRA_MESSAGE = "message";

    private final int ID_DAILY_REMINDER = 101;

    public DailyReminderReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra(EXTRA_MESSAGE);

        String title = TYPE_REMINDER;
        int notifId = ID_DAILY_REMINDER;

        showToast(context, title, msg);
        showAlarmNotifications(context, title, msg, notifId);
    }

    private void showToast(Context context, String title, String msg) {
        Toast.makeText(context, title+" : "+msg, Toast.LENGTH_SHORT).show();
    }

    public void setRemindAlarm(Context context, String msg) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, msg);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //check if the time was not greater than current time
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            //add one day if true, so the alarm manager will executed next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Toast.makeText(context, "alarm will start tomorrow at 07.00 AM", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "daily reminder alarm set up", Toast.LENGTH_SHORT).show();
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 24 * 60 * 60, pendingIntent);
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, DailyReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_DAILY_REMINDER, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Daily Reminder was canceled", Toast.LENGTH_SHORT).show();
    }

    private void showAlarmNotifications(Context context, String title, String msg, int notifId) {
        String CHANNEL_ID = "Channel_1";
        String CHANNEL_NAME = "AlarmManager channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_movie_black_24dp)
                .setContentTitle(title)
                .setContentText(msg)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        Notification notification = builder.build();
        if (notificationManager != null) {
            notificationManager.notify(notifId, notification);
        }
    }
}
