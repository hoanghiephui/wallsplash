package com.unsplash.wallsplash._common.i.view;

/**
 * Swipe back manage view.
 */

public interface SwipeBackManageView {

    boolean checkCanSwipeBack(int dir);

    void swipeBackFinish();
}
