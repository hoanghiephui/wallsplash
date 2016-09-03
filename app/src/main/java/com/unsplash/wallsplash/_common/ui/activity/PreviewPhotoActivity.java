package com.unsplash.wallsplash._common.ui.activity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.utils.DisplayUtils;
import com.unsplash.wallsplash._common.utils.LanguageUtils;
import com.unsplash.wallsplash._common.utils.NotificationUtils;
import com.unsplash.wallsplash._common.utils.ThemeUtils;

/**
 * Created by Hoang Hiep on 9/3/2016.
 */

public class PreviewPhotoActivity extends AppCompatActivity
        implements View.OnClickListener, NotificationUtils.SnackbarContainer {
    // widget
    private CoordinatorLayout container;
    private LinearLayout widgetContainer;
    private LinearLayout iconContainer;

    // data
    private Photo photo;
    private boolean showPreview = false;
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
        setContentView(R.layout.activity_preview_photo);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!started) {
            started = true;
            initData();
            initView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WallSplashApplication.getInstance().removeActivity();
    }

    private void setTheme() {
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            setTheme(R.style.MysplashTheme_light_Translucent_PhotoPreview);
        } else {
            setTheme(R.style.MysplashTheme_dark_Translucent_PhotoPreview);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    private void loadLanguage() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String language = sharedPreferences.getString(
                getString(R.string.key_language),
                getResources().getStringArray(R.array.language_values)[0]);
        LanguageUtils.setLanguage(this, language);
    }

    /**
     * <br> view.
     */

    // init.
    private void initView() {
        this.container = (CoordinatorLayout) findViewById(R.id.activity_preview_photo_container);

        PhotoView photoView = (PhotoView) findViewById(R.id.activity_preview_photo_photoView);
        photoView.setMaxScale(calcMaxScale());
        photoView.enable();
        photoView.setOnClickListener(this);
        Glide.with(this)
                .load(photo.urls.regular)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(photoView);

        this.widgetContainer = (LinearLayout) findViewById(R.id.activity_preview_photo_widgetContainer);
        this.iconContainer = (LinearLayout) findViewById(R.id.activity_preview_photo_iconContainer);
    }

    // anim.

    private void showIcons() {
        TranslateAnimation show = new TranslateAnimation(0, 0, 0, -iconContainer.getMeasuredHeight());
        show.setFillEnabled(true);
        show.setFillAfter(true);
        show.setDuration(200);
        iconContainer.clearAnimation();
        iconContainer.startAnimation(show);
    }

    private void hideIcons() {
        TranslateAnimation hide = new TranslateAnimation(0, 0, -iconContainer.getMeasuredHeight(), 0);
        hide.setFillEnabled(true);
        hide.setFillAfter(true);
        hide.setDuration(200);
        iconContainer.clearAnimation();
        iconContainer.startAnimation(hide);
    }

    private void showWidget() {
        TranslateAnimation show = new TranslateAnimation(0, 0, 0, widgetContainer.getMeasuredHeight());
        show.setFillEnabled(true);
        show.setFillAfter(true);
        show.setDuration(200);
        widgetContainer.clearAnimation();
        widgetContainer.startAnimation(show);
    }

    private void hideWidget() {
        TranslateAnimation hide = new TranslateAnimation(0, 0, widgetContainer.getMeasuredHeight(), 0);
        hide.setFillEnabled(true);
        hide.setFillAfter(true);
        hide.setDuration(200);
        widgetContainer.clearAnimation();
        widgetContainer.startAnimation(hide);
    }

    /**
     * <br> data.
     */

    private void initData() {
        this.photo = WallSplashApplication.getInstance().getPhoto();
    }

    private float calcMaxScale() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels
                + DisplayUtils.getNavigationBarHeight(this);

        float screenRatio = (float) (1.0 * screenWidth / screenHeight);
        float photoRatio = (float) (1.0 * photo.width / photo.height);

        if (screenRatio >= photoRatio) {
            return (float) (screenWidth / (1.0 * photo.width * screenHeight / photo.height));
        } else {
            return (float) (screenHeight / (1.0 * photo.height * screenWidth / photo.width));
        }
    }

    /**
     * <br> interface.
     */

    // on click listener.
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_preview_photo_photoView:
                if (showPreview) {
                    showPreview = false;
                    hideWidget();
                    hideIcons();
                } else {
                    showPreview = true;
                    showWidget();
                    showIcons();
                }
                break;
        }
    }

    // snackbar container.

    @Override
    public View getSnackbarContainer() {
        return container;
    }
}
