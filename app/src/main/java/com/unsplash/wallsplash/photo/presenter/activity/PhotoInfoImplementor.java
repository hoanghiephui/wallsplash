package com.unsplash.wallsplash.photo.presenter.activity;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.data.PhotoDetails;
import com.unsplash.wallsplash.common.data.data.User;
import com.unsplash.wallsplash.common.i.model.PhotoInfoModel;
import com.unsplash.wallsplash.common.i.presenter.PhotoInfoPresenter;
import com.unsplash.wallsplash.common.i.view.PhotoInfoView;

/**
 * Photo info implementor.
 */

public class PhotoInfoImplementor
        implements PhotoInfoPresenter {
    // model & view.
    private PhotoInfoModel model;
    private PhotoInfoView view;

    /**
     * <br> life cycle.
     */

    public PhotoInfoImplementor(PhotoInfoModel model, PhotoInfoView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void touchAuthorAvatar() {
        User u = User.buildUser(model.getPhoto());
        WallSplashApplication.getInstance().setUser(u);
        view.touchAuthorAvatar();
    }

    @Override
    public void touchMenuItem(int itemId) {
        view.touchMenuItem(itemId);
    }

    @Override
    public void drawPhotoDetails() {
        view.drawPhotoDetails(model.getPhotoDetails());
    }

    @Override
    public Photo getPhoto() {
        return model.getPhoto();
    }

    @Override
    public PhotoDetails getPhotoDetails() {
        return model.getPhotoDetails();
    }
}
