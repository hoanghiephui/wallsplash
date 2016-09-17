package com.unsplash.wallsplash.common.i.model;

import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.data.PhotoDetails;
import com.unsplash.wallsplash.common.data.service.PhotoService;

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
