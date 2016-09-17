package com.unsplash.wallsplash.main.presenter.fragment;

import android.content.Context;
import android.view.View;

import com.unsplash.wallsplash.common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash.common.i.view.PopupManageView;
import com.unsplash.wallsplash.common.ui.popup.SearchOrientationPopupWindow;

/**
 * Search fragment popup manage implementor.
 */

public class SearchFragmentPopupManageImplementor
        implements PopupManagePresenter {
    // model & view.
    private PopupManageView view;

    /**
     * <br> life cycle.
     */

    public SearchFragmentPopupManageImplementor(PopupManageView view) {
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void showPopup(Context c, View anchor, String value, int position) {
        SearchOrientationPopupWindow window = new SearchOrientationPopupWindow(
                c,
                anchor,
                value);
        window.setOnSearchOrientationChangedListener(new SearchOrientationPopupWindow.OnSearchOrientationChangedListener() {
            @Override
            public void onSearchOrientationChanged(String orientationValue) {
                view.responsePopup(orientationValue, 0);
            }
        });
    }
}
