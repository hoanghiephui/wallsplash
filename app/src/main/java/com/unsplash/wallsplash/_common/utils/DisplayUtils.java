package com.unsplash.wallsplash._common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.unsplash.wallsplash.JniBitmapHolder;
import com.unsplash.wallsplash.R;

/**
 * Display utils.
 */

public class DisplayUtils {
    // data
    private int dpi = 0;

    /**
     * <br> life cycle.
     */

    public DisplayUtils(Context context) {
        dpi = context.getResources().getDisplayMetrics().densityDpi;
    }

    public float dpToPx(int dp) {
        if (dpi == 0) {
            return 0;
        }
        return (float) (dp * (dpi / 160.0));
    }

    public static int getStatusBarHeight(Resources r) {
        int result = 0;
        int resourceId = r.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = r.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(Context c) {
        boolean hasMenuKey = ViewConfiguration.get(c).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            Resources resources = c.getResources();
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static void setWindowTop(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
            final JniBitmapHolder bitmapHolder = new JniBitmapHolder(icon);
            icon.recycle();
            icon = bitmapHolder.getBitmapAndFree();
            if (ThemeUtils.getInstance(activity).isLightTheme()) {
                ActivityManager.TaskDescription taskDescription
                        = new ActivityManager.TaskDescription(
                        activity.getString(R.string.app_name),
                        icon,
                        ContextCompat.getColor(activity, R.color.colorPrimary_light));
                activity.setTaskDescription(taskDescription);
            } else {
                ActivityManager.TaskDescription taskDescription
                        = new ActivityManager.TaskDescription(
                        activity.getString(R.string.app_name),
                        icon,
                        ContextCompat.getColor(activity, R.color.colorPrimary_dark));
                activity.setTaskDescription(taskDescription);
            }
            icon.recycle();
        }
    }

    public static void setStatusBarTextDark(Activity activity) {
        if (ThemeUtils.getInstance(activity).isNeedSetStatusBarTextDark()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }
}
