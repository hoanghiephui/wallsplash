package com.unsplash.wallsplash.common.i.view;

import com.unsplash.wallsplash.common.data.data.PhotoDetails;

/**
 * Photo details view.
 */

public interface PhotoDetailsView {

    void drawExif(PhotoDetails details);

    void initRefreshStart();

    void requestDetailsSuccess();
}
