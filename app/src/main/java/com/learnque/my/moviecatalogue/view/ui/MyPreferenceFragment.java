package com.learnque.my.moviecatalogue.view.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.learnque.my.moviecatalogue.R;
import com.learnque.my.moviecatalogue.service.alarm.DailyReminderReceiver;
import com.learnque.my.moviecatalogue.service.alarm.ReleaseReminderReceiver;
import com.learnque.my.moviecatalogue.service.model.MovieTv;
import java.util.ArrayList;

public class MyPreferenceFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ReleaseReminderReceiver releaseReceiver;
    private DailyReminderReceiver dailyReceiver;

    private String RELEASE, DAILY;

    private SwitchPreference daily, release;

    public static ArrayList<MovieTv> data = new ArrayList<>();

    public static void setData(ArrayList<MovieTv> data) {
        MyPreferenceFragment.data = data;
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        init();
    }

    private void init() {
        RELEASE = getResources().getString(R.string.key_release);
        DAILY = getResources().getString(R.string.key_daily);

        release = (SwitchPreference) findPreference(RELEASE);
        daily = (SwitchPreference) findPreference(DAILY);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(RELEASE)) {
            releaseReceiver = new ReleaseReminderReceiver();
            if (release.isChecked()) {
                releaseReceiver.setReleaseAlarm(getContext());
            } else {
                releaseReceiver.cancelAlarm(getContext());
            }
        }
        if (key.equals(DAILY)) {
            dailyReceiver = new DailyReminderReceiver();
            if (daily.isChecked()) {
                dailyReceiver.setRemindAlarm(getActivity().getApplicationContext(),"Let's See what's new today!");
            } else {
                dailyReceiver.cancelAlarm(getActivity().getApplicationContext());
            }
        }
    }
}