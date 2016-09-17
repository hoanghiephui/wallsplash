package com.unsplash.wallsplash.common.i.view;

/**
 * Scroll view.
 */

public interface ScrollView {

    void scrollToTop();

    void autoLoad(int dy);

    boolean needBackToTop();
}
