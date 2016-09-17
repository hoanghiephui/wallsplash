package com.unsplash.wallsplash.photo.presenter.activity;

import com.unsplash.wallsplash.WallSplashApplication;
import com.unsplash.wallsplash.common.data.data.Photo;
import com.unsplash.wallsplash.common.data.tools.DownloadManager;
import com.unsplash.wallsplash.common.i.model.DownloadModel;
import com.unsplash.wallsplash.common.i.presenter.DownloadPresenter;
import com.unsplash.wallsplash.common.i.view.DownloadView;
import com.unsplash.wallsplash.common.utils.FileUtils;
import com.unsplash.wallsplash.photo.model.activity.DownloadObject;

/**
 * Download implementor.
 */

public class DownloadImplementor
        implements DownloadPresenter, DownloadManager.OnDownloadListener {
    // model & view.
    private DownloadModel model;
    private DownloadView view;

    /**
     * <br> life cycle.
     */

    public DownloadImplementor(DownloadModel model, DownloadView view) {
        this.model = model;
        this.view = view;
    }

    /**
     * <br> presenter.
     */

    @Override
    public void download() {
        model.setDownloadType(DownloadObject.DOWNLOAD_TYPE);
        doDownload(DownloadManager.DOWNLOAD_TYPE);
    }

    @Override
    public void share() {
        model.setDownloadType(DownloadObject.SHARE_TYPE);
        doDownload(DownloadManager.SHARE_TYPE);
    }

    @Override
    public void setWallpaper() {
        model.setDownloadType(DownloadObject.WALLPAPER_TYPE);
        doDownload(DownloadManager.WALLPAPER_TYPE);
    }

    @Override
    public void setDialogShowing(boolean showing) {
        model.setDialogShowing(showing);
    }

    @Override
    public void cancelDownloading() {
        DownloadManager.getInstance().cancel(((Photo) model.getDownloadKey()).id);
        model.setDownloading(false);
    }

    @Override
    public int getDownloadId() {
        return model.getDownloadId();
    }

    @Override
    public void setDownloadId(int id) {
        model.setDownloadId(id);
    }

    /**
     * <br> utils.
     */

    private void doDownload(int type) {
        if (FileUtils.createDownloadPath(WallSplashApplication.getInstance())) {
            int id = DownloadManager.getInstance().add(
                    (Photo) model.getDownloadKey(),
                    type,
                    this);
            if (id != DownloadManager.FAILED_CODE) {
                model.setDownloadId(id);
                model.setDownloading(true);
                model.setDialogShowing(true);
                view.showDownloadDialog();
            }
        }
    }

    /**
     * <br> interface.
     */

    @Override
    public void onDownloadComplete(int id) {
        if (model.getDownloadId() == id && model.isDialogShowing()) {
            view.dismissDownloadDialog();
            model.setDownloading(false);
        }
    }

    @Override
    public void onDownloadFailed(int id, int code) {
        if (model.getDownloadId() == id && model.isDialogShowing()) {
            view.dismissDownloadDialog();
            model.setDownloading(false);
        }
    }

    @Override
    public void onDownloadProgress(int id, int percent) {
        if (model.getDownloadId() == id && model.isDialogShowing()) {
            view.onDownloadProcess(percent);
        }
    }
}
