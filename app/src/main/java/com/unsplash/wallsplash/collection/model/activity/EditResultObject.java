package com.unsplash.wallsplash.collection.model.activity;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.i.model.EditResultModel;

/**
 * Edit result object.
 */

public class EditResultObject
        implements EditResultModel {
    // data
    private Object key;

    /**
     * <br> life cycle.
     */

    public EditResultObject() {
        this.key = WallSplashApplication.getInstance().getCollection();
    }

    /**
     * <br> model.
     */

    @Override
    public Object getEditKey() {
        return key;
    }

    @Override
    public void setEditKey(Object k) {
        key = k;
    }
}
