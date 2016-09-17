package com.unsplash.wallsplash.photo.model.widget;

import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.data.PhotoDetails;
import com.unsplash.wallsplash.common.data.service.PhotoService;
import com.unsplash.wallsplash.common.i.model.PhotoDetailsModel;

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

    public PhotoDetailsObject(Photo p) {
        service = PhotoService.getService();
        photo = p;
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
