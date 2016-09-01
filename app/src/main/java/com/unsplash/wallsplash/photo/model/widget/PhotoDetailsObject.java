package com.unsplash.wallsplash.photo.model.widget;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.data.PhotoDetails;
import com.unsplash.wallsplash._common.data.service.PhotoService;
import com.unsplash.wallsplash._common.i.model.PhotoDetailsModel;

/**
 * Photo details object.
 */

public class PhotoDetailsObject
        implements PhotoDetailsModel {
    // data
    private PhotoService service;
    private Photo photo;
    private PhotoDetails photoDetails;

    /**
     * <br> life cycle.
     */

    public PhotoDetailsObject() {
        service = PhotoService.getService();
        photo = WallSplashApplication.getInstance().getPhoto();
    }

    /**
     * <br> model.
     */

    @Override
    public PhotoService getService() {
        return service;
    }

    @Override
    public Photo getPhoto() {
        return photo;
    }

    @Override
    public void setPhoto(Photo p) {
        photo = p;
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
