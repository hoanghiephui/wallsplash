package com.unsplash.wallsplash._common.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash._common.ui.fragment.SettingsFragment;
import com.unsplash.wallsplash._common.ui.widget.StatusBarView;
import com.unsplash.wallsplash._common.utils.ThemeUtils;

/**
 * Settings activity.
 */

public class SettingsActivity extends BaseActivity
        implements View.OnClickListener {
    // widget
    private CoordinatorLayout container;

    /**
     * <br> life cycle.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isStarted()) {
            setStarted();
            initWidget();
            getFragmentManager()
                    .beginTransaction()
                    .setTransition(android.app.FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.activity_settings_preferenceContainer, new SettingsFragment())
                    .commit();
        }
    }

    @Override
    protected void setTheme() {
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            setTheme(R.style.MysplashTheme_light_Translucent_Settings);
        } else {
            setTheme(R.style.MysplashTheme_dark_Translucent_Settings);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.activity_slide_out_bottom);
    }

    /**
     * <br> UI..
     */

    private void initWidget() {
        StatusBarView statusBar = (StatusBarView) findViewById(R.id.activity_settings_statusBar);
        if (ThemeUtils.getInstance(this).isNeedSetStatusBarMask()) {
            statusBar.setMask(true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_settings_toolbar);
        if (ThemeUtils.getInstance(this).isLightTheme()) {
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_light);
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_toolbar_back_dark);
        }
        toolbar.setNavigationOnClickListener(this);

        this.container = (CoordinatorLayout) findViewById(R.id.activity_settings_container);
    }

    /**
     * <br> interface.
     */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case -1:
                finish();
                break;
        }
    }

    @Override
    public View getSnackbarContainer() {
        return container;
    }
}
