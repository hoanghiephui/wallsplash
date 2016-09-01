package com.unsplash.wallsplash._common.i.view;

/**
 * Scroll view.
 */

public interface ScrollView {

    void scrollToTop();

    void autoLoad(int dy);

    boolean needBackToTop();
}
