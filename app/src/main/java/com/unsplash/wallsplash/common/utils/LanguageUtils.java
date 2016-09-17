package com.unsplash.wallsplash.common.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.unsplash.wallsplash.R;

import java.util.Locale;

/**
 * Language utils.
 */

public class LanguageUtils {

    public static void setLanguage(Context c, String key) {
        if (key.equals(c.getResources().getStringArray(R.array.language_values)[0])) {
            return;
        }
        Resources resources = c.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        switch (key) {
            case "english":
                configuration.setLocale(Locale.US);
                break;

            /*case "chinese":
                configuration.setLocale(Locale.SIMPLIFIED_CHINESE);
                break;*/

            default:
                configuration.setLocale(Locale.US);
                break;
        }
        resources.updateConfiguration(configuration, metrics);
    }
}
