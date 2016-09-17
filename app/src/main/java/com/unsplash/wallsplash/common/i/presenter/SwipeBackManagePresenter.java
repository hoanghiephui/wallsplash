package com.unsplash.wallsplash.common.i.presenter;

/**
 * Swipe back manage presenter.
 */

public interface SwipeBackManagePresenter {

    boolean checkCanSwipeBack(int dir);

    void swipeBackFinish(int dir);
}
