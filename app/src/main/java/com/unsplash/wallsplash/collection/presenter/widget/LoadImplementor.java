package com.unsplash.wallsplash.collection.presenter.widget;

import com.unsplash.wallsplash.collection.model.widget.LoadObject;
import com.unsplash.wallsplash.common.i.model.LoadModel;
import com.unsplash.wallsplash.common.i.presenter.LoadPresenter;
import com.unsplash.wallsplash.common.i.view.LoadView;

/**
 * Load implementor.
 */

public class LoadImplementor
        implements LoadPresenter {
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
            case LoadObject.FAILED_STATE:
                model.setState(LoadObject.LOADING_STATE);
                view.setLoadingState();
                break;

            case LoadObject.NORMAL_STATE:
                model.setState(LoadObject.LOADING_STATE);
                view.resetLoadingState();
                break;
        }
    }

    @Override
    public void setFailedState() {
        if (model.getState() == LoadObject.LOADING_STATE) {
            model.setState(LoadObject.FAILED_STATE);
            view.setFailedState();
        }
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
