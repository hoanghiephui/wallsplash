package com.unsplash.wallsplash.user.presenter.widget;

import com.unsplash.wallsplash._common.data.data.User;
import com.unsplash.wallsplash._common.data.service.UserService;
import com.unsplash.wallsplash._common.i.model.UserModel;
import com.unsplash.wallsplash._common.i.presenter.UserPresenter;
import com.unsplash.wallsplash._common.i.view.UserView;

import retrofit2.Call;
import retrofit2.Response;

/**
 * User implementor.
 */

public class UserImplementor
        implements UserPresenter,
        UserService.OnRequestUserProfileListener {
    // model & view.
    private UserModel model;
    private UserView view;

    /**
     * <br> life cycle.
     */

    public UserImplementor(UserModel model, UserView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void requestUser() {
        view.initRefreshStart();
        model.getService().requestUserProfile(model.getUser().username, this);
    }

    @Override
    public void cancelRequest() {
        model.getService().cancel();
    }

    /**
     * <br> interface.
     */

    @Override
    public void onRequestUserProfileSuccess(Call<User> call, Response<User> response) {
        if (response.isSuccessful() && response.body() != null) {
            model.setUser(response.body());
            view.drawUserInfo(response.body());
            view.requestDetailsSuccess();
        } else {
            requestUser();
        }
    }

    @Override
    public void onRequestUserProfileFailed(Call<User> call, Throwable t) {
        requestUser();
    }
}
