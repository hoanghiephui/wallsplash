package com.unsplash.wallsplash.common.i.presenter;

import android.support.v7.app.AppCompatActivity;

/**
 * Message mange presenter.
 */

public interface MessageManagePresenter {

    void sendMessage(int what, Object o);

    void responseMessage(AppCompatActivity activity, int what, Object o);
}
