package com.unsplash.wallsplash._common.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;

import java.util.List;

/**
 * Snackbar utils.
 */

public class NotificationUtils {

    public static void showSnackbar(String content, int duration) {
        List<AppCompatActivity> activityList = WallSplashApplication.getInstance().getActivityList();
        if (activityList != null && activityList.size() != 0) {
            Context c = activityList.get(activityList.size() - 1);
            View container = ((SnackbarContainer) activityList.get(activityList.size() - 1)).getSnackbarContainer();

            Snackbar snackbar = Snackbar
                    .make(container, content, duration);

            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

            TextView contentTxt = (TextView) snackbarLayout.findViewById(R.id.snackbar_text);
            TypefaceUtils.setTypeface(c, contentTxt);

            if (ThemeUtils.getInstance(c).isLightTheme()) {
                contentTxt.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_light);
            } else {
                contentTxt.setTextColor(ContextCompat.getColor(c, R.color.colorTextContent_dark));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_dark);
            }

            snackbar.show();
        }
    }

    public static void showActionSnackbar(String content, String action,
                                          int duration, View.OnClickListener l) {
        List<AppCompatActivity> activityList = WallSplashApplication.getInstance().getActivityList();
        if (activityList != null && activityList.size() != 0) {
            Context c = activityList.get(activityList.size() - 1);
            View container = ((SnackbarContainer) activityList.get(activityList.size() - 1)).getSnackbarContainer();

            Snackbar snackbar = Snackbar
                    .make(container, content, duration)
                    .setAction(action, l);

            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();

            TextView contentTxt = (TextView) snackbarLayout.findViewById(R.id.snackbar_text);
            TypefaceUtils.setTypeface(c, contentTxt);

            Button actionBtn = (Button) snackbarLayout.findViewById(R.id.snackbar_action);

            if (ThemeUtils.getInstance(c).isLightTheme()) {
                contentTxt.setTextColor(ContextCompat.getColor(c, R.color.colorWhite));
                actionBtn.setTextColor(ContextCompat.getColor(c, R.color.colorAccent_light));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_light);
            } else {
                contentTxt.setTextColor(ContextCompat.getColor(c, R.color.colorTextContent_dark));
                actionBtn.setTextColor(ContextCompat.getColor(c, R.color.colorAccent_light));
                snackbarLayout.setBackgroundResource(R.color.colorPrimary_dark);
            }

            snackbar.show();
        }
    }

    public interface SnackbarContainer {
        View getSnackbarContainer();
    }
}
