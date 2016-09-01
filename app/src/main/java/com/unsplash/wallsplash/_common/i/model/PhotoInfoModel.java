package com.unsplash.wallsplash._common.i.model;

import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.data.PhotoDetails;

/**
 * Photo info model.
 */

public interface PhotoInfoModel {

    Photo getPhoto();

    PhotoDetails getPhotoDetails();

    void setPhotoDetails(PhotoDetails details);
}
