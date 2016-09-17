package com.unsplash.wallsplash.main.presenter.fragment;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import com.unsplash.wallsplash.common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash.common.i.view.PopupManageView;
import com.unsplash.wallsplash.common.ui.popup.PhotoOrderPopupWindow;

/**
 * Category fragment popup manage implementor.
 */

public class CategoryFragmentPopupManageImplementor
        implements PopupManagePresenter,
        PhotoOrderPopupWindow.OnPhotoOrderChangedListener {
    // model & view.
    private PopupManageView view;

    /**
     * <br> life cycle.
     */

    public CategoryFragmentPopupManageImplementor(PopupManageView view) {
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void showPopup(Context context, View anchor, String value, int position) {
        PhotoOrderPopupWindow window = new PhotoOrderPopupWindow(
                context,
                anchor,
                value,
                PhotoOrderPopupWindow.CATEGORY_TYPE);
        window.setOnPhotoOrderChangedListener(this);
    }

    @Override
    public void onPhotoOrderChange(String orderValue) {
        view.responsePopup(orderValue, 0);
    }
}
