package com.unsplash.wallsplash.main.presenter.activity;

import com.unsplash.wallsplash.R;
import com.unsplash.wallsplash.common.i.model.DrawerModel;
import com.unsplash.wallsplash.common.i.presenter.DrawerPresenter;
import com.unsplash.wallsplash.common.i.view.DrawerView;

/**
 * Drawer implementor.
 */

public class DrawerImplementor
        implements DrawerPresenter {
    // model & view.
    private DrawerModel model;
    private DrawerView view;

    /**
     * <br> life cycle.
     */

    public DrawerImplementor(DrawerModel model, DrawerView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void touchNavItem(int id) {
        if (model.getSelectedItemId() != id) {
            view.touchNavItem(id);
        }
        if (id != R.id.action_change_theme
                && id != R.id.action_download_manage
                && id != R.id.action_settings
                && id != R.id.action_about) {
            model.setSelectedItemId(id);
        }
    }
}
