package com.unsplash.wallsplash.common.i.presenter;

/**
 * Message mange presenter.
 */

public interface MessageManagePresenter {

    void sendMessage(int what, Object o);

    void responseMessage(int what, Object o);
}
