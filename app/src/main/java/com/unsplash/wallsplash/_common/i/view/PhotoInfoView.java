package com.unsplash.wallsplash._common.i.view;

import com.unsplash.wallsplash._common.data.data.PhotoDetails;

/**
 * Photo info view.
 */

public interface PhotoInfoView {

    void touchAuthorAvatar();

    void touchMenuItem(int itemId);

    void drawPhotoDetails(PhotoDetails details);
}
