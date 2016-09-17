package com.unsplash.wallsplash.common.i.view;

import android.view.View;

/**
 * Load view.
 */

public interface LoadView {

    void animShow(View v);

    void animHide(View v);

    void setLoadingState();

    void setFailedState();

    void setNormalState();

    void resetLoadingState();
}
