package com.unsplash.wallsplash._common.i.model;

import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.data.PhotoDetails;
import com.unsplash.wallsplash._common.data.service.PhotoService;

/**
 * Photo details model.
 */

public interface PhotoDetailsModel {

    PhotoService getService();

    Photo getPhoto();

    void setPhoto(Photo p);

    PhotoDetails getPhotoDetails();

    void setPhotoDetails(PhotoDetails details);
}
