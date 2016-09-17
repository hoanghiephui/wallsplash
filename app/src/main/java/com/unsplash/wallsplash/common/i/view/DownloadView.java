package com.unsplash.wallsplash.common.i.view;

/**
 * Download view.
 */

public interface DownloadView {

    void showDownloadDialog();

    void dismissDownloadDialog();

    void onDownloadProcess(int progress);
}
