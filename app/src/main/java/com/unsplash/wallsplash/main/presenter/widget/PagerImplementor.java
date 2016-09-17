package com.unsplash.wallsplash.main.presenter.widget;

import com.unsplash.wallsplash.common.i.presenter.PagerPresenter;
import com.unsplash.wallsplash.common.i.view.PagerView;

/**
 * Pager implementor.
 */

public class PagerImplementor
        implements PagerPresenter {
    // model & view.
    private PagerView view;

    /**
     * <br> life cycle.
     */

    public PagerImplementor(PagerView view) {
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public boolean checkNeedRefresh() {
        return view.checkNeedRefresh();
    }

    @Override
    public void refreshPager() {
        view.refreshPager();
    }
}
