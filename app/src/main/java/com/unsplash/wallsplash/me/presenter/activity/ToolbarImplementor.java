package com.unsplash.wallsplash.me.presenter.activity;

import com.unsplash.wallsplash._common.i.presenter.ToolbarPresenter;
import com.unsplash.wallsplash._common.i.view.ToolbarView;

/**
 * Toolbar implementor.
 */

public class ToolbarImplementor
        implements ToolbarPresenter {
    // model & view.
    private ToolbarView view;

    /**
     * <br>
     */

    public ToolbarImplementor(ToolbarView view) {
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void touchNavigatorIcon() {
        view.touchNavigatorIcon();
    }

    @Override
    public void touchToolbar() {
        view.touchToolbar();
    }

    @Override
    public void touchMenuItem(int itemId) {
        view.touchMenuItem(itemId);
    }
}
