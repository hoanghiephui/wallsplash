package com.unsplash.wallsplash.common.i.presenter;

/**
 * Download presenter.
 */

public interface DownloadPresenter {

    void download();

    void share();

    void setWallpaper();

    void setDialogShowing(boolean showing);

    void cancelDownloading();

    int getDownloadId();

    void setDownloadId(int id);
}
