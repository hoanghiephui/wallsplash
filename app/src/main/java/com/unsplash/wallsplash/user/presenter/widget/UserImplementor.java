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
        implements UserPresenter {
    // model & view.
    private UserModel model;
    private UserView view;

    // data
    private OnRequestUserProfileListener listener;

    /**
     * <br> life cycle.
     */

    public UserImplementor(UserModel model, UserView view) {
        this.model = model;
        this.view = view;
    }

    /** <br> presenter. */

    @Override
    public void requestUser() {
        view.initRefreshStart();
        listener = new OnRequestUserProfileListener();
        model.getService().requestUserProfile(model.getUser().username, listener);
    }

    @Override
    public void cancelRequest() {
        if (listener != null) {
            listener.cancel();
        }
        model.getService().cancel();
    }

    /** <br> interface. */

    private class OnRequestUserProfileListener implements UserService.OnRequestUserProfileListener {
        // data
        private boolean canceled;

        OnRequestUserProfileListener() {
            canceled = false;
        }

        public void cancel() {
            canceled = true;
        }

        @Override
        public void onRequestUserProfileSuccess(Call<User> call, Response<User> response) {
            if (canceled) {
                return;
            }
            if (response.isSuccessful() && response.body() != null) {
                model.setUser(response.body());
                view.drawUserInfo(response.body());
                view.requestDetailsSuccess();
            } else if (Integer.parseInt(response.headers().get("X-Ratelimit-Remaining")) < 0) {
                RateLimitDialog dialog = new RateLimitDialog();
                dialog.show(
                        WallSplashApplication.getInstance().getLatestActivity().getSupportFragmentManager(),
                        null);
            } else {
                requestUser();
            }
        }

        @Override
        public void onRequestUserProfileFailed(Call<User> call, Throwable t) {
            if (canceled) {
                return;
            }
            requestUser();
        }
    }
}
