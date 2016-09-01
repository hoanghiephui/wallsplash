package com.unsplash.wallsplash.me.presenter.activity;

import com.unsplash.wallsplash._common.i.presenter.SwipeBackManagePresenter;
import com.unsplash.wallsplash._common.i.view.SwipeBackManageView;

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
    public void swipeBackFinish() {
        view.swipeBackFinish();
    }
}

