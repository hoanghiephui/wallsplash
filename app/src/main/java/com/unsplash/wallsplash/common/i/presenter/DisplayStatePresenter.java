package com.unsplash.wallsplash.common.i.presenter;

/**
 * Display state presenter.
 */

public interface DisplayStatePresenter {

    void setLoadingState();

    void setFailedState();

    void setNormalState();
}
