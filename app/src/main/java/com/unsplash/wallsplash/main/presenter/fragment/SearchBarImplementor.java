package com.unsplash.wallsplash.main.presenter.fragment;

import android.app.Activity;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.presenter.SearchBarPresenter;
import com.unsplash.wallsplash.common.i.view.SearchBarView;
import com.unsplash.wallsplash.main.view.activity.MainActivity;

/**
 * Search bar implementor.
 */

public class SearchBarImplementor
        implements SearchBarPresenter {
    // models & view.
    private SearchBarView view;

    /**
     * <br> life cycle.
     */

    public SearchBarImplementor(SearchBarView view) {
        this.view = view;
    }

    /** <br> presenter. */

    @Override
    public void touchNavigatorIcon(Activity a) {
        ((MainActivity) a).removeFragment();
    }

    @Override
    public boolean touchMenuItem(Activity a, int itemId) {
        switch (itemId) {
            case R.id.action_clear_text:
                view.clearSearchBarText();
                break;
        }
        return true;
    }

    @Override
    public void showKeyboard() {
        view.showKeyboard();
    }

    @Override
    public void hideKeyboard() {
        view.hideKeyboard();
    }

    @Override
    public void submitSearchInfo(String text) {
        view.submitSearchInfo(text);
    }
}
