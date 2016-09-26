package com.unsplash.wallsplash.common.i.presenter;

import android.app.Activity;

/**
 * Swipe back manage presenter.
 */

public interface SwipeBackManagePresenter {

    boolean checkCanSwipeBack(int dir);

    void swipeBackFinish(Activity activity, int dir);
}
