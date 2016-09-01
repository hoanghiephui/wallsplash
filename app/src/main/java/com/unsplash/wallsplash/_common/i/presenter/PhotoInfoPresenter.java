package com.unsplash.wallsplash._common.i.presenter;

import com.unsplash.wallsplash._common.data.data.Photo;
import com.unsplash.wallsplash._common.data.data.PhotoDetails;

/**
 * Photo info presenter.
 */

public interface PhotoInfoPresenter {

    void touchAuthorAvatar();

    void touchMenuItem(int itemId);

    void drawPhotoDetails();

    Photo getPhoto();

    PhotoDetails getPhotoDetails();
}
