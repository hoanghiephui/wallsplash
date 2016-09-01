package com.unsplash.wallsplash.user.presenter.widget;

import com.unsplash.wallsplash._common.i.model.LoadModel;
import com.unsplash.wallsplash._common.i.presenter.LoadPresenter;
import com.unsplash.wallsplash._common.i.view.LoadView;
import com.unsplash.wallsplash.user.model.widget.LoadObject;

/**
 * Load implementor.
 */

public class LoadImplementor implements LoadPresenter {
    // model & view.
    private LoadModel model;
    private LoadView view;

    /**
     * <br> life cycle.
     */

    public LoadImplementor(LoadModel model, LoadView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void setLoadingState() {
        switch (model.getState()) {
            case LoadObject.NORMAL_STATE:
                model.setState(LoadObject.LOADING_STATE);
                view.resetLoadingState();
                break;
        }
    }

    @Override
    public void setFailedState() {
        // do nothing.
    }

    @Override
    public void setNormalState() {
        if (model.getState() == LoadObject.LOADING_STATE) {
            model.setState(LoadObject.NORMAL_STATE);
            view.setNormalState();
        }
    }

    @Override
    public int getLoadState() {
        return model.getState();
    }
}
