package com.unsplash.wallsplash.me.model.activity;

import com.unsplash.wallsplash.common.i.model.PagerManageModel;

/**
 * Pager manage object.
 */

public class PagerManageObject
        implements PagerManageModel {
    // data
    private int pagePosition;

    /**
     * <br> life cycle.
     */

    public PagerManageObject(int initPosition) {
        this.pagePosition = initPosition;
    }

    /**
     * <br> model.
     */

    @Override
    public int getPagerPosition() {
        return pagePosition;
    }

    @Override
    public void setPagerPosition(int position) {
        pagePosition = position;
    }
}
