package com.unsplash.wallsplash.common.utils;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;

/**
 * Snackbar utils.
 */

public class NotificationUtils {

    public static void showSnackbar(String content, int duration) {
        if (WallSplashApplication.getInstance().getActivityCount() > 0) {
            Activity activity = WallSplashApplication.getInstance().getLatestActivity();
            View container = ((SnackbarContainer) activity).getSnackbarContainer();

            Snackbar snackbar = Snackbar
                    .make(container, content, duration);

            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

            TextView contentTxt = (TextView) snackbarLayout.findViewById(R.id.snackbar_text);
            TypefaceUtils.setTypeface(activity, contentTxt);

            if (ThemeUtils.getInstance(activity).isLightTheme()) {
                contentTxt.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_light);
            } else {
                contentTxt.setTextColor(ContextCompat.getColor(activity, R.color.colorTextContent_dark));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_dark);
            }

            snackbar.show();
        }
    }

    public static void showActionSnackbar(String content, String action,
                                          int duration, View.OnClickListener l) {
        if (WallSplashApplication.getInstance().getActivityCount() > 0) {
            Activity activity = WallSplashApplication.getInstance().getLatestActivity();
            View container = ((SnackbarContainer) activity).getSnackbarContainer();

            Snackbar snackbar = Snackbar
                    .make(container, content, duration)
                    .setAction(action, l);

            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

            TextView contentTxt = (TextView) snackbarLayout.findViewById(R.id.snackbar_text);
            TypefaceUtils.setTypeface(activity, contentTxt);

            Button actionBtn = (Button) snackbarLayout.findViewById(R.id.snackbar_action);

            if (ThemeUtils.getInstance(activity).isLightTheme()) {
                contentTxt.setTextColor(ContextCompat.getColor(activity, R.color.colorWhite));
                actionBtn.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent_light));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_light);
            } else {
                contentTxt.setTextColor(ContextCompat.getColor(activity, R.color.colorTextContent_dark));
                actionBtn.setTextColor(ContextCompat.getColor(activity, R.color.colorAccent_light));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_dark);
            }

            snackbar.show();
        }
    }

    public interface SnackbarContainer {
        View getSnackbarContainer();
    }
}
