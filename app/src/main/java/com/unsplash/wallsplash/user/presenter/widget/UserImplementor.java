package com.unsplash.wallsplash.user.presenter.widget;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.data.service.UserService;
import com.unsplash.wallsplash.common.i.model.UserModel;
import com.unsplash.wallsplash.common.i.presenter.UserPresenter;
import com.unsplash.wallsplash.common.i.view.UserView;
import com.unsplash.wallsplash.common.ui.dialog.RateLimitDialog;

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
        } else if (Integer.parseInt(response.headers().get("X-Ratelimit-Remaining")) < 0) {
            RateLimitDialog dialog = new RateLimitDialog();
            dialog.show(
                    WallSplashApplication.getInstance().getActivityList().get(
                            WallSplashApplication.getInstance().getActivityList().size()).getSupportFragmentManager(),
                    null);
        } else {
            requestUser();
        }
    }

    @Override
    public void onRequestUserProfileFailed(Call<User> call, Throwable t) {
        requestUser();
    }
}
