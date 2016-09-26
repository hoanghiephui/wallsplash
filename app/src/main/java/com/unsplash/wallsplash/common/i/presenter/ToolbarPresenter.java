package com.unsplash.wallsplash.common.i.presenter;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

/**
 * Toolbar presenter.
 */

public interface ToolbarPresenter {

    void touchNavigatorIcon(Activity activity);

    void touchToolbar(Activity activity);

    boolean touchMenuItem(AppCompatActivity activity, int itemId);
}
