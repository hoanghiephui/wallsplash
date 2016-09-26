package com.unsplash.wallsplash.me.presenter.activity;

import android.app.Activity;
import android.os.Build;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.presenter.SwipeBackManagePresenter;
import com.unsplash.wallsplash.common.i.view.SwipeBackManageView;
import com.unsplash.wallsplash.common.ui.widget.SwipeBackLayout;

/**
 * Swipe back manage implementor.
 */

public class SwipeBackManageImplementor
        implements SwipeBackManagePresenter {
    // model & view.
    private SwipeBackManageView view;

    /**
     * <br> life cycle.
     */

    public SwipeBackManageImplementor(SwipeBackManageView view) {
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public boolean checkCanSwipeBack(int dir) {
        return view.checkCanSwipeBack(dir);
    }

    @Override
    public void swipeBackFinish(Activity a, int dir) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            a.finishAfterTransition();
        } else {
            a.finish();
            switch (dir) {
                case SwipeBackLayout.UP_DIR:
                    a.overridePendingTransition(0, R.anim.activity_slide_out_top);
                    break;

                case SwipeBackLayout.DOWN_DIR:
                    a.overridePendingTransition(0, R.anim.activity_slide_out_bottom);
                    break;
            }
        }
    }
}

