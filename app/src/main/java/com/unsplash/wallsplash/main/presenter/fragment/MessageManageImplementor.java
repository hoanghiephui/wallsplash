package com.unsplash.wallsplash.main.presenter.fragment;

import android.support.v7.app.AppCompatActivity;

import com.unsplash.wallsplash.common.i.presenter.MessageManagePresenter;
import com.unsplash.wallsplash.common.i.view.MessageManageView;

/**
 * Created by Hoang Hiep on 9/25/2016.
 */

public class MessageManageImplementor
        implements MessageManagePresenter {
    // model & view.
    private MessageManageView view;

    /**
     * <br> life cycle.
     */

    public MessageManageImplementor(MessageManageView view) {
        this.view = view;
    }

    @Override
    public void sendMessage(int what, Object o) {
        view.sendMessage(what, o);
    }

    @Override
    public void responseMessage(final AppCompatActivity a, int what, Object o) {
        switch (what) {
            case 1:
                view.responseMessage(what, o);
                break;
        }
    }
}
