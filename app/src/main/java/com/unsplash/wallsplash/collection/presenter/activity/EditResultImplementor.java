package com.unsplash.wallsplash.collection.presenter.activity;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Collection;
import com.unsplash.wallsplash._common.i.model.EditResultModel;
import com.unsplash.wallsplash._common.i.presenter.EditResultPresenter;
import com.unsplash.wallsplash._common.i.view.EditResultView;

/**
 * Edit result implementor.
 */

public class EditResultImplementor
        implements EditResultPresenter {
    // model & view.
    private EditResultModel model;
    private EditResultView view;

    /**
     * <br> life cycle.
     */

    public EditResultImplementor(EditResultModel model, EditResultView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void createSomething(Object newKey) {
        // do nothing.
    }

    @Override
    public void updateSomething(Object newKey) {
        model.setEditKey(newKey);
        WallSplashApplication.getInstance().setCollection((Collection) newKey);
        view.drawUpdateResult(newKey);
    }

    @Override
    public void deleteSomething(Object oldKey) {
        WallSplashApplication.getInstance().setCollection((Collection) oldKey);
        view.drawDeleteResult(oldKey);
    }

    @Override
    public Object getEditKey() {
        return model.getEditKey();
    }
}
