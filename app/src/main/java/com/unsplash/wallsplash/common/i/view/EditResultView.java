package com.unsplash.wallsplash.common.i.view;

/**
 * Edit result view.
 */

public interface EditResultView {

    void drawCreateResult(Object newKey);

    void drawUpdateResult(Object newKey);

    void drawDeleteResult(Object oldKey);
}