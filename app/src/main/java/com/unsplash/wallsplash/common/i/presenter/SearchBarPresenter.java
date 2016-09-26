package com.unsplash.wallsplash.common.i.presenter;

import android.app.Activity;

/**
 * Search bar presenter.
 */

public interface SearchBarPresenter {

    void touchNavigatorIcon(Activity activity);

    boolean touchMenuItem(Activity activity, int itemId);

    void showKeyboard();
    void hideKeyboard();

    void submitSearchInfo(String text);
}
