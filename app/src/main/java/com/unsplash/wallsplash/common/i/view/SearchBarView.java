package com.unsplash.wallsplash.common.i.view;

/**
 * Search bar view.
 */

public interface SearchBarView {

    void touchNavigatorIcon();

    void touchMenuItem(int itemId);

    void touchOrientationIcon();

    void touchSearchBar();

    void showKeyboard();

    void hideKeyboard();

    void submitSearchInfo(String text, String orientation);
}
