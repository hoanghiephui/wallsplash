package com.unsplash.wallsplash.photo.presenter.activity;

import com.unsplash.wallsplash._common.i.model.ScrollModel;
import com.unsplash.wallsplash._common.i.presenter.ScrollPresenter;
import com.unsplash.wallsplash._common.i.view.ScrollView;

/**
 * Scroll implementor.
 */

public class ScrollImplementor
        implements ScrollPresenter {
    // model & view.
    private ScrollModel model;
    private ScrollView view;

    /**
     * <br> life cycle.
     */

    public ScrollImplementor(ScrollModel model, ScrollView view) {
        this.model = model;
        this.view = view;
    }

    @Override
    public boolean isToTop() {
        return model.isToTop();
    }

    @Override
    public void setToTop(boolean top) {
        model.setToTop(top);
    }

    /**
     * <br> presenter.
     */

    @Override
    public void scrollToTop() {
        view.scrollToTop();
    }

    @Override
    public void autoLoad(int dy) {
        view.autoLoad(dy);
    }

    @Override
    public boolean needBackToTop() {
        return view.needBackToTop();
    }
}
