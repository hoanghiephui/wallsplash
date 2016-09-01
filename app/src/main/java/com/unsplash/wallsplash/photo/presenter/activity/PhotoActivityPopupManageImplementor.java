package com.unsplash.wallsplash.photo.presenter.activity;

import android.content.Context;
import android.view.View;

import com.unsplash.wallsplash._common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash._common.i.view.PopupManageView;
import com.unsplash.wallsplash._common.ui.popup.PhotoMenuPopupWindow;

/**
 * Photo activity popup manage implementor.
 */

public class PhotoActivityPopupManageImplementor
        implements PopupManagePresenter,
        PhotoMenuPopupWindow.OnSelectItemListener {
    // model & view.
    private PopupManageView view;

    /**
     * <br> life cycle.
     */

    public PhotoActivityPopupManageImplementor(PopupManageView view) {
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void showPopup(Context c, View anchor, String value, int position) {
        PhotoMenuPopupWindow window = new PhotoMenuPopupWindow(c, anchor);
        window.setOnSelectItemListener(this);
    }

    @Override
    public void onSelectItem(int id) {
        view.responsePopup(null, id);
    }
}
