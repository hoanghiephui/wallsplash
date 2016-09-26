package com.unsplash.wallsplash.collection.presenter.activity;

import android.app.Activity;

import com.unsplash.wallsplash.collection.view.activity.CollectionActivity;
import com.unsplash.wallsplash.common.i.presenter.SwipeBackManagePresenter;
import com.unsplash.wallsplash.common.i.view.SwipeBackManageView;

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
        ((CollectionActivity) a).finishActivity(dir, false);
    }
}
