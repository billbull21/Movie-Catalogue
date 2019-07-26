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
import android.util.Log;
import android.widget.Toast;

import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.model.AlarmItem;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class ReleaseReminderReceiver extends BroadcastReceiver {

    public static final String TYPE_RELEASE = "The film That was Released Today!";
    private static final String API_KEY = "48662cea933b55e0480a5d4d76ef7fbb";

    private final int ID_RELEASE_REMINDER = 201;

    @Override
    public void onReceive(Context context, Intent intent) {
        AsyncHttpClient client = new AsyncHttpClient();
        final Context context1 = context;
        final ArrayList<AlarmItem> listData = new ArrayList<>();
        Log.d("List1", String.valueOf(listData));

        String url = "https://api.themoviedb.org/3/discover/movie?api_key="+API_KEY+"&language=en-US";
        Log.d("List2", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject resps = new JSONObject(result);
                    JSONArray list = resps.getJSONArray("results");
                    Log.d("List", ""+list.length());
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject data = list.getJSONObject(i);
                        AlarmItem model = new AlarmItem();
                        model.setId(data.getInt("id"));
                        model.setTitle(data.getString("title"));
                        model.setRelease(data.getString("release_date"));
                        listData.add(model);
                    }
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date releaseDate = new Date();
                    String todayDate = simpleDateFormat.format(releaseDate);
                    for (AlarmItem item : listData) {
                        if (item.getRelease().equals(todayDate)) {
                            showAlarmNotifications(context1, TYPE_RELEASE, item.getTitle(), item.getId());
                        }
                    }
                } catch (Exception e) {
                    Log.d("REPOSITORY", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("REPOSITORY", "GAGALLLLLLL!!!");
            }
        });
    }

    public void setReleaseAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminderReceiver.class);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //check if the time was not greater than current time
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            //add one day if true, so the alarm manager will executed next day
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            Toast.makeText(context, "alarm will start tomorrow at 08.00 AM", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "release reminder alarm set up", Toast.LENGTH_SHORT).show();
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, 0);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 24 * 60 * 60, pendingIntent);
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE_REMINDER, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Toast.makeText(context, "Release Reminder was canceled", Toast.LENGTH_SHORT).show();
    }

    private void showAlarmNotifications(Context context, String title, String msg, int notifId) {
        String CHANNEL_ID = "Channel_2";
        String CHANNEL_NAME = "ReleaseReminder channel";

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
