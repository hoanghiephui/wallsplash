package com.unsplash.wallsplash.main.model.activity;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash._common.i.model.DrawerModel;

/**
 * Drawer object.
 */

public class DrawerObject
        implements DrawerModel {
    // data
    private int selectedId;

    /**
     * <br> life cycle.
     */

    public DrawerObject() {
        selectedId = R.id.action_home;
    }

    /**
     * <br> model.
     */

    @Override
    public int getSelectedItemId() {
        return selectedId;
    }

    @Override
    public void setSelectedItemId(int id) {
        selectedId = id;
    }
}
