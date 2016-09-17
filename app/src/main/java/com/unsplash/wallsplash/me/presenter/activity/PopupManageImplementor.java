package com.unsplash.wallsplash.me.presenter.activity;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.presenter.PopupManagePresenter;
import com.unsplash.wallsplash.common.i.view.PopupManageView;
import com.unsplash.wallsplash.common.ui.popup.PhotoOrderPopupWindow;
import com.unsplash.wallsplash.common.utils.NotificationUtils;

/**
 * Popup manage implementor.
 */

public class PopupManageImplementor
        implements PopupManagePresenter {
    // model & view.
    private PopupManageView view;

    /**
     * <br> life cycle.
     */

    public PopupManageImplementor(PopupManageView view) {
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void showPopup(Context c, View anchor, String value, final int position) {
        if (position % 2 == 0) {
            PhotoOrderPopupWindow window = new PhotoOrderPopupWindow(
                    c,
                    anchor,
                    value,
                    PhotoOrderPopupWindow.NO_RANDOM_TYPE);
            window.setOnPhotoOrderChangedListener(new PhotoOrderPopupWindow.OnPhotoOrderChangedListener() {
                @Override
                public void onPhotoOrderChange(String orderValue) {
                    view.responsePopup(orderValue, position);
                }
            });
        } else {
            NotificationUtils.showSnackbar(
                    c.getString(R.string.feedback_no_filter),
                    Snackbar.LENGTH_SHORT);
        }
    }
}
