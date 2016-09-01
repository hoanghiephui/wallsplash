package com.unsplash.wallsplash.photo.model.activity;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.data.PhotoDetails;
import com.unsplash.wallsplash._common.i.model.PhotoInfoModel;

/**
 * Photo info object.
 */

public class PhotoInfoObject
        implements PhotoInfoModel {
    // data
    private Photo photo;
    private PhotoDetails photoDetails;

    /**
     * <br> life cycle.
     */

    public PhotoInfoObject() {
        photo = WallSplashApplication.getInstance().getPhoto();
    }

    @Override
    public Photo getPhoto() {
        return photo;
    }

    @Override
    public PhotoDetails getPhotoDetails() {
        return photoDetails;
    }

    @Override
    public void setPhotoDetails(PhotoDetails details) {
        photoDetails = details;
    }
}
