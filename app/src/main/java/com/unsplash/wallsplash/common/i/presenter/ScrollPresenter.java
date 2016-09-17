package com.unsplash.wallsplash.common.i.presenter;

/**
 * Scroll presenter.
 */

public interface ScrollPresenter {

    boolean isToTop();

    void setToTop(boolean top);

    void scrollToTop();

    void autoLoad(int dy);

    boolean needBackToTop();
}
