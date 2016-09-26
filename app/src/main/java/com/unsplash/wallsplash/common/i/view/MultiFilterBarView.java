package com.unsplash.wallsplash.common.i.view;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public interface MultiFilterBarView {
    void touchMenuContainer(int position);

    void showKeyboard();

    void hideKeyboard();

    void submitSearchInfo(int categoryId, boolean featured,
                          String username, String query,
                          String orientation);
}
