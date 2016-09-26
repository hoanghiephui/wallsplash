package com.unsplash.wallsplash.common.i.presenter;

import android.app.Activity;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public interface MultiFilterBarPresenter {
    void touchNavigatorIcon(Activity activity);

    void touchMenuContainer(int position);

    void showKeyboard();

    void hideKeyboard();

    void submitSearchInfo(int categoryId, boolean featured,
                          String username, String query,
                          String orientation);

    void setQuery(String query);

    String getQuery();

    void setUsername(String username);

    String getUsername();

    void setCategory(int c);

    int getCategory();

    void setOrientation(String o);

    String getOrientation();

    void setFeatured(boolean f);

    boolean isFeatured();
}
