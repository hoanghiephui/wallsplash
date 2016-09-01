package com.unsplash.wallsplash._common.i.view;

import com.unsplash.wallsplash._common.data.data.PhotoDetails;

/**
 * Photo details view.
 */

public interface PhotoDetailsView {

    void drawExif(PhotoDetails details);

    void initRefreshStart();

    void requestDetailsSuccess();
}
