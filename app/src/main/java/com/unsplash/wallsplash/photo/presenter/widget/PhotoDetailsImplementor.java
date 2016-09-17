package com.unsplash.wallsplash.photo.presenter.widget;

import android.content.Context;
import android.support.design.widget.Snackbar;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.PhotoDetails;
import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.i.model.PhotoDetailsModel;
import com.unsplash.wallsplash.common.i.presenter.PhotoDetailsPresenter;
import com.unsplash.wallsplash.common.i.view.PhotoDetailsView;
import com.unsplash.wallsplash.common.ui.dialog.RateLimitDialog;
import com.unsplash.wallsplash.common.utils.NotificationUtils;
import com.unsplash.wallsplash.common.utils.ValueUtils;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Photo details implementor.
 */

public class PhotoDetailsImplementor
        implements PhotoDetailsPresenter {
    // model & view.
    private PhotoDetailsModel model;
    private PhotoDetailsView view;

    /**
     * <br> life cycle.
     */

    public PhotoDetailsImplementor(PhotoDetailsModel model, PhotoDetailsView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void requestPhotoDetails(Context c) {
        view.initRefreshStart();
        model.getService()
                .requestPhotoDetails(model.getPhoto(), new OnRequestPhotoDetailsListener(c));
    }

    @Override
    public void cancelRequest() {
        model.getService().cancel();
    }

    @Override
    public void showExifDescription(Context c, String title, String content) {
        NotificationUtils.showSnackbar(
                title + " : " + content,
                Snackbar.LENGTH_SHORT);
    }

    /**
     * <br> interface.
     */

    private class OnRequestPhotoDetailsListener implements PhotoService.OnRequestPhotoDetailsListener {
        // data
        private Context c;

        public OnRequestPhotoDetailsListener(Context c) {
            this.c = c;
        }

        @Override
        public void onRequestPhotoDetailsSuccess(Call<PhotoDetails> call, Response<PhotoDetails> response) {
            if (response.isSuccessful() && response.body() != null) {
                ValueUtils.writePhotoCount(
                        c,
                        response.body());
                model.setPhotoDetails(response.body());
                view.drawExif(model.getPhotoDetails());
                view.requestDetailsSuccess();
            } else {
                requestPhotoDetails(c);
                RateLimitDialog.checkAndNotify(
                        WallSplashApplication.getInstance().getActivityList().get(WallSplashApplication.getInstance().getActivityList().size() - 1),
                        response.headers().get("X-Ratelimit-Remaining"));
            }
        }

        @Override
        public void onRequestPhotoDetailsFailed(Call<PhotoDetails> call, Throwable t) {
            requestPhotoDetails(c);
        }
    }
}
