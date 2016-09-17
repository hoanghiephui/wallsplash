package com.unsplash.wallsplash.common.i.view;

/**
 * Swipe back manage view.
 */

public interface SwipeBackManageView {

    boolean checkCanSwipeBack(int dir);

    void swipeBackFinish(int dir);
}
