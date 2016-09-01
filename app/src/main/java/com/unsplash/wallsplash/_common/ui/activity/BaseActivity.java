package com.unsplash.wallsplash._common.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.utils.DisplayUtils;
import com.unsplash.wallsplash._common.utils.LanguageUtils;
import com.unsplash.wallsplash._common.utils.NotificationUtils;

/**
 * WallSplashApplication Activity
 */

public abstract class BaseActivity extends AppCompatActivity
        implements NotificationUtils.SnackbarContainer {
    // data.
    private boolean started = false;

    /**
     * <br> life cycle.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WallSplashApplication.getInstance().addActivity(this);
        loadLanguage();
        setTheme();
        DisplayUtils.setWindowTop(this);
        DisplayUtils.setStatusBarTextDark(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WallSplashApplication.getInstance().removeActivity();
    }

    /**
     * <br> data.
     */

    public void setStarted() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    private void loadLanguage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPreferences.getString(
                getString(R.string.key_language),
                getResources().getStringArray(R.array.language_values)[0]);
        LanguageUtils.setLanguage(this, language);
    }

    protected abstract void setTheme();
}
