package com.unsplash.wallsplash.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.ui.activity.SettingsActivity;

/**
 * Created by Hoang Hiep on 9/3/2016.
 */

public class BackToTopUtils {
    // data
    private String backValue;
    private boolean notified;

    /**
     * <br> life cycle.
     */

    private BackToTopUtils(Context c) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        backValue = sharedPreferences.getString(c.getString(R.string.key_back_to_top), "all");
        notified = sharedPreferences.getBoolean(c.getString(R.string.key_notified_set_back_to_top), false);
    }

    public void changeBackValue(String newValue) {
        backValue = newValue;
    }

    /**
     * <br> UI.
     */

    public void showSetBackToTopSnackbar() {
        final Context c = WallSplashApplication.getInstance()
                .getActivityList()
                .get(WallSplashApplication.getInstance().getActivityList().size() - 1);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(c);
        if (!notified) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(
                    c.getString(R.string.key_notified_set_back_to_top),
                    true);
            editor.apply();

            notified = true;

            NotificationUtils.showActionSnackbar(
                    c.getString(R.string.feedback_notify_set_back_to_top),
                    c.getString(R.string.set),
                    Snackbar.LENGTH_LONG,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent s = new Intent(c, SettingsActivity.class);
                            c.startActivity(s);
                        }
                    });
        }
    }

    /**
     * <br> data.
     */

    public boolean isSetBackToTop(boolean home) {
        if (home) {
            return !backValue.equals("none");
        } else {
            return backValue.equals("all");
        }
    }

    public boolean isNotified() {
        return notified;
    }

    /**
     * <br> singleton.
     */

    private static BackToTopUtils instance;

    public static synchronized BackToTopUtils getInstance(Context c) {
        if (instance == null) {
            instance = new BackToTopUtils(c);
        }
        return instance;
    }
}
